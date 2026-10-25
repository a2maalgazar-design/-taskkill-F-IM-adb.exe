package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val code: String,
    val nameAr: String,
    val nameEn: String,
    val icon: String,
    val recurrence: String = "weekly", // "daily", "weekly", "monthly"
    val scheduleTiming: String = "",
    val defaultLeadTimeHours: Int = 24,
    val defaultAssigneeId: String = "user-assistant-01",
    val defaultSupplierPhone: String = "+201011122233",
    val sheetColumn: Int = 1,
    val isActive: Boolean = true
)
