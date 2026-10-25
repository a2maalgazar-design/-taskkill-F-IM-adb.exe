package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "receiving_inspections",
    indices = [Index(value = ["orderId"])]
)
data class ReceivingInspectionEntity(
    @PrimaryKey
    val id: String, // e.g. rec-1729...
    val orderId: String,
    val receivedAt: String,
    val receivedByUserId: String,
    val receivedByUserName: String,
    val packageCondition: String, // "intact", "acceptable", "damaged", "opened"
    val productCondition: String, // "fresh_compliant", "minor_issues", "non_compliant_rejected"
    val temperatureCelsius: Double?,
    val isHaccpCompliant: Boolean,
    val inspectionResult: String, // "ACCEPTED", "PARTIALLY_ACCEPTED", "REJECTED"
    val rejectionReason: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
