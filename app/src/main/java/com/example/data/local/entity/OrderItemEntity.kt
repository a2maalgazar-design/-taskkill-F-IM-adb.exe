package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    indices = [Index(value = ["orderId"]), Index(value = ["productId"])]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val productId: String,
    val productName: String,
    val unit: String,
    val parLevel: Double = 0.0,
    val requestedQty: Double = 0.0,
    val orderedQty: Double = 0.0,
    val receivedQty: Double = 0.0,
    val rejectedQty: Double = 0.0,
    val rejectionReason: String = "",
    val itemStatus: String = "pending" // "pending", "ordered", "received_full", "received_partial", "rejected"
)
