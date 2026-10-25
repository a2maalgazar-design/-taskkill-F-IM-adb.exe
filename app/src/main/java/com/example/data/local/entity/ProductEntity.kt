package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index(value = ["categoryId"]), Index(value = ["sheetColumn"])]
)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val nameAr: String,
    val unit: String,
    val parLevel: Double = 10.0, // average daily demand
    val sheetColumn: Int = 1,
    val sheetRow: Int = 1,
    val isActive: Boolean = true
)
