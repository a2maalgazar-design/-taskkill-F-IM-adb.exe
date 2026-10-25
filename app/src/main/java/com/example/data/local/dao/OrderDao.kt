package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

data class OrderWithItems(
    val order: OrderEntity,
    val items: List<OrderItemEntity>
)

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun getAllActiveOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: String): OrderEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getItemsForOrder(orderId: String): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItemsForOrderSync(orderId: String): List<OrderItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Update
    suspend fun updateOrderItem(item: OrderItemEntity)

    @Query("UPDATE orders SET status = :newStatus, updatedAt = :timestamp WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, newStatus: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE orders SET isArchived = 1, updatedAt = :timestamp WHERE id = :orderId")
    suspend fun archiveOrder(orderId: String, timestamp: Long = System.currentTimeMillis())

    @Transaction
    suspend fun insertCompleteOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        insertOrder(order)
        insertOrderItems(items)
    }

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int
}
