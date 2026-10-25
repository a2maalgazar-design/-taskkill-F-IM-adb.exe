package com.example.data.repository

import com.example.data.local.KomsDatabase
import com.example.data.local.SeedDataProvider
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReceivingInspectionEntity
import com.example.data.local.entity.SystemSettingsEntity
import com.example.data.local.entity.UserEntity
import com.example.util.WorkWeekUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class KitchenRepository(private val database: KomsDatabase) {

    private val userDao = database.userDao()
    private val categoryDao = database.categoryDao()
    private val productDao = database.productDao()
    private val orderDao = database.orderDao()
    private val receivingDao = database.receivingDao()
    private val settingsDao = database.settingsDao()

    suspend fun ensureDataInitialized() = withContext(Dispatchers.IO) {
        KomsDatabase.populateInitialData(database)
    }

    // --- Users & Staff ---
    val allActiveUsers: Flow<List<UserEntity>> = userDao.getAllActiveUsers()
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUserById(id: String): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserById(id)
    }

    suspend fun saveUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(id: String) = withContext(Dispatchers.IO) {
        userDao.deleteUser(id)
    }

    // --- Categories ---
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun saveCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(id: String) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategory(id)
    }

    // --- Products ---
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(categoryId)
    }

    suspend fun saveProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(id: String) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(id)
    }

    // --- Orders ---
    val allActiveOrders: Flow<List<OrderEntity>> = orderDao.getAllActiveOrders()

    fun getOrderItems(orderId: String): Flow<List<OrderItemEntity>> {
        return orderDao.getItemsForOrder(orderId)
    }

    suspend fun getOrderById(id: String): OrderEntity? = withContext(Dispatchers.IO) {
        orderDao.getOrderById(id)
    }

    suspend fun getOrderItemsSync(orderId: String): List<OrderItemEntity> = withContext(Dispatchers.IO) {
        orderDao.getItemsForOrderSync(orderId)
    }

    suspend fun createOrder(
        categoryId: String,
        categoryNameAr: String,
        user: UserEntity,
        recipientType: String,
        recipientPhone: String,
        itemsWithQuantities: List<Pair<ProductEntity, Double>>,
        notes: String = ""
    ): String = withContext(Dispatchers.IO) {
        val totalCount = orderDao.getOrdersCount() + 1
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val formattedId = "KOMS-$year-${String.format("%05d", totalCount)}"
        val todayIso = WorkWeekUtil.getTodayDateIso()
        val workWeek = WorkWeekUtil.getWorkWeekString()

        val orderEntity = OrderEntity(
            id = formattedId,
            orderNumber = formattedId,
            categoryId = categoryId,
            categoryNameAr = categoryNameAr,
            orderDate = todayIso,
            orderingDeadline = "${todayIso}T12:00:00",
            fixedArrivalHour = "13:00",
            primaryAssigneeId = user.id,
            primaryAssigneeName = user.name,
            recipientType = recipientType,
            recipientPhone = recipientPhone,
            status = "ORDERED",
            notes = notes,
            totalItemsCount = itemsWithQuantities.size,
            workWeek = workWeek
        )

        val itemEntities = itemsWithQuantities.map { (prod, qty) ->
            OrderItemEntity(
                orderId = formattedId,
                productId = prod.id,
                productName = prod.nameAr,
                unit = prod.unit,
                parLevel = prod.parLevel,
                requestedQty = qty,
                orderedQty = qty,
                receivedQty = 0.0,
                rejectedQty = 0.0,
                itemStatus = "ordered"
            )
        }

        orderDao.insertCompleteOrder(orderEntity, itemEntities)
        formattedId
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, newStatus)
    }

    suspend fun archiveOrder(orderId: String) = withContext(Dispatchers.IO) {
        orderDao.archiveOrder(orderId)
    }

    // --- Receiving & QA ---
    val allInspections: Flow<List<ReceivingInspectionEntity>> = receivingDao.getAllInspections()

    suspend fun recordReceiving(
        orderId: String,
        inspector: UserEntity,
        packageCondition: String,
        productCondition: String,
        temperatureCelsius: Double?,
        inspectionResult: String,
        rejectionReason: String,
        notes: String,
        receivedItems: List<Triple<OrderItemEntity, Double, Double>> // item, receivedQty, rejectedQty
    ) = withContext(Dispatchers.IO) {
        val inspectionId = "rec-${System.currentTimeMillis()}"
        val isCompliant = (temperatureCelsius == null || temperatureCelsius <= 4.0) && inspectionResult != "REJECTED"

        val inspection = ReceivingInspectionEntity(
            id = inspectionId,
            orderId = orderId,
            receivedAt = WorkWeekUtil.getTodayDateIso(),
            receivedByUserId = inspector.id,
            receivedByUserName = inspector.name,
            packageCondition = packageCondition,
            productCondition = productCondition,
            temperatureCelsius = temperatureCelsius,
            isHaccpCompliant = isCompliant,
            inspectionResult = inspectionResult,
            rejectionReason = rejectionReason,
            notes = notes
        )

        receivingDao.insertInspection(inspection)

        // Update item quantities
        for ((item, receivedQty, rejectedQty) in receivedItems) {
            val status = when {
                receivedQty >= item.orderedQty -> "received_full"
                receivedQty > 0 -> "received_partial"
                rejectedQty > 0 -> "rejected"
                else -> item.itemStatus
            }
            orderDao.updateOrderItem(
                item.copy(
                    receivedQty = receivedQty,
                    rejectedQty = rejectedQty,
                    rejectionReason = rejectionReason,
                    itemStatus = status
                )
            )
        }

        // Determine new overall status
        val nextStatus = when (inspectionResult) {
            "ACCEPTED" -> "COMPLETED"
            "PARTIALLY_ACCEPTED" -> "PARTIALLY_RECEIVED"
            "REJECTED" -> "PARTIALLY_REJECTED"
            else -> "COMPLETED"
        }

        val nowIso = WorkWeekUtil.getTodayDateIso()
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            orderDao.updateOrder(
                order.copy(
                    status = nextStatus,
                    actualDeliveryTime = nowIso,
                    receivingChefArrivalTime = nowIso,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    // --- Settings ---
    val settingsFlow: Flow<SystemSettingsEntity?> = settingsDao.observeSettings("default")

    suspend fun getSettings(): SystemSettingsEntity = withContext(Dispatchers.IO) {
        settingsDao.getSettings("default") ?: SeedDataProvider.getInitialSettings().also {
            settingsDao.saveSettings(it)
        }
    }

    suspend fun saveSettings(settings: SystemSettingsEntity) = withContext(Dispatchers.IO) {
        settingsDao.saveSettings(settings)
    }

    suspend fun resetCatalogToDefault() = withContext(Dispatchers.IO) {
        categoryDao.insertCategories(SeedDataProvider.getInitialCategories())
        productDao.insertProducts(SeedDataProvider.getInitialProducts())
    }
}
