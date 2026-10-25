package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String, // e.g. KOMS-2026-000001
    val orderNumber: String,
    val categoryId: String,
    val categoryNameAr: String,
    val orderDate: String, // YYYY-MM-DD
    val orderingDeadline: String,
    val fixedArrivalHour: String = "13:00",
    val actualDeliveryTime: String? = null,
    val receivingChefArrivalTime: String? = null,
    val primaryAssigneeId: String,
    val primaryAssigneeName: String,
    val recipientType: String = "supplier", // "supplier" or "purchasing"
    val recipientPhone: String = "",
    val status: String = "ORDERED", // "PLANNED", "ORDERED", "AWAITING_DELIVERY", "RECEIVING", "COMPLETED", "PARTIALLY_RECEIVED", "PARTIALLY_REJECTED", "CANCELLED"
    val priority: String = "normal",
    val notes: String = "",
    val totalItemsCount: Int = 0,
    val workWeek: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)
