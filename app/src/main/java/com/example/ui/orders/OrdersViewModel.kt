package com.example.ui.orders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SystemSettingsEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.KitchenRepository
import com.example.util.WhatsAppUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OrdersUiState(
    val selectedCategoryId: String = "cat-veg",
    val searchQuery: String = "",
    val showOnlyFilled: Boolean = false,
    val selectedRecurrenceFilter: String = "ALL", // "ALL", "daily", "weekly", "monthly"
    val quantities: Map<String, Double> = emptyMap(),
    val recipientTypes: Map<String, String> = emptyMap(), // categoryId -> "supplier" or "purchasing"
    val recentCreatedOrderId: String? = null,
    val lastDispatchedWhatsAppMessage: String? = null,
    val showDispatchDialog: Boolean = false,
    val dispatchTargetPhone: String = "",
    val dispatchChefPhone: String = "",
    val dispatchCategoryName: String = "",
    val dispatchTotalItems: Int = 0,
    val toastMessage: String? = null
)

class OrdersViewModel(private val repository: KitchenRepository) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeOrders: StateFlow<List<OrderEntity>> = repository.allActiveOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    fun selectCategory(categoryId: String) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleShowOnlyFilled() {
        _uiState.value = _uiState.value.copy(showOnlyFilled = !_uiState.value.showOnlyFilled)
    }

    fun setRecurrenceFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedRecurrenceFilter = filter)
    }

    fun setQuantity(productId: String, qty: Double) {
        val next = _uiState.value.quantities.toMutableMap()
        if (qty <= 0.0) {
            next.remove(productId)
        } else {
            next[productId] = qty
        }
        _uiState.value = _uiState.value.copy(quantities = next)
    }

    fun stepQuantity(productId: String, delta: Double) {
        val current = _uiState.value.quantities[productId] ?: 0.0
        val nextQty = (current + delta).coerceAtLeast(0.0)
        setQuantity(productId, nextQty)
    }

    fun fillParLevelsForCategory(categoryId: String) {
        val categoryProducts = allProducts.value.filter { it.categoryId == categoryId && it.isActive }
        val next = _uiState.value.quantities.toMutableMap()
        categoryProducts.forEach { p ->
            next[p.id] = if (p.parLevel > 0) p.parLevel else 10.0
        }
        _uiState.value = _uiState.value.copy(
            quantities = next,
            toastMessage = "تم ملء الأصناف بالمعدل اليومي القياسي"
        )
    }

    fun clearCategoryQuantities(categoryId: String) {
        val categoryProducts = allProducts.value.filter { it.categoryId == categoryId }
        val next = _uiState.value.quantities.toMutableMap()
        categoryProducts.forEach { p ->
            next.remove(p.id)
        }
        _uiState.value = _uiState.value.copy(
            quantities = next,
            toastMessage = "تم مسح كميات القسم"
        )
    }

    fun clearAllQuantities() {
        _uiState.value = _uiState.value.copy(
            quantities = emptyMap(),
            toastMessage = "تم مسح جميع الكميات"
        )
    }

    fun setRecipientType(categoryId: String, type: String) {
        val next = _uiState.value.recipientTypes.toMutableMap()
        next[categoryId] = type
        _uiState.value = _uiState.value.copy(recipientTypes = next)
    }

    fun dismissDispatchDialog() {
        _uiState.value = _uiState.value.copy(showDispatchDialog = false)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }

    fun prepareAndDispatchOrder(
        context: Context,
        currentUser: UserEntity,
        categoryId: String,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val category = categories.value.find { it.id == categoryId } ?: return@launch
            val categoryProducts = allProducts.value.filter { it.categoryId == categoryId && it.isActive }
            val filledItems = categoryProducts.mapNotNull { prod ->
                val qty = _uiState.value.quantities[prod.id]
                if (qty != null && qty > 0) {
                    prod to qty
                } else null
            }

            if (filledItems.isEmpty()) {
                _uiState.value = _uiState.value.copy(toastMessage = "الرجاء تحديد كمية لصنف واحد على الأقل قبل الإرسال")
                return@launch
            }

            val settings = repository.getSettings()
            val recipientType = _uiState.value.recipientTypes[categoryId] ?: "supplier"
            val targetPhone = if (recipientType == "purchasing") {
                settings.purchasingPhone
            } else {
                category.defaultSupplierPhone.ifBlank { "+201011122233" }
            }
            val chefPhone = settings.headChefPhone.ifBlank { "+201000000000" }

            val orderId = repository.createOrder(
                categoryId = category.id,
                categoryNameAr = category.nameAr,
                user = currentUser,
                recipientType = recipientType,
                recipientPhone = targetPhone,
                itemsWithQuantities = filledItems,
                notes = notes
            )

            val dispatchedItems = filledItems.map { (prod, qty) ->
                WhatsAppUtil.DispatchedOrderItem(
                    name = prod.nameAr,
                    qty = qty,
                    unit = prod.unit
                )
            }

            val whatsappMessage = WhatsAppUtil.buildKitchenOrderMessage(
                orderCode = orderId,
                categoryName = category.nameAr,
                requesterName = currentUser.name,
                items = dispatchedItems,
                notes = notes
            )

            // Open WhatsApp immediately to target supplier
            WhatsAppUtil.openWhatsApp(context, targetPhone, whatsappMessage)

            // Also prepare dispatch confirmation dialog with chef CC option
            _uiState.value = _uiState.value.copy(
                recentCreatedOrderId = orderId,
                lastDispatchedWhatsAppMessage = whatsappMessage,
                showDispatchDialog = true,
                dispatchTargetPhone = targetPhone,
                dispatchChefPhone = chefPhone,
                dispatchCategoryName = category.nameAr,
                dispatchTotalItems = filledItems.size,
                toastMessage = "تم حفظ الطلبية $orderId وتجهيز إرسال الواتساب"
            )
        }
    }

    fun sendChefCcWhatsApp(context: Context) {
        val message = _uiState.value.lastDispatchedWhatsAppMessage ?: return
        val chefPhone = _uiState.value.dispatchChefPhone
        WhatsAppUtil.openWhatsApp(context, chefPhone, message)
    }
}

class OrdersViewModelFactory(private val repository: KitchenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrdersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
