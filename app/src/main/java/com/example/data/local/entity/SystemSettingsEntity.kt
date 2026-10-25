package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_settings")
data class SystemSettingsEntity(
    @PrimaryKey
    val key: String, // e.g. "phones"
    val purchasingPhone: String = "+201099887766",
    val headChefPhone: String = "+201000000000",
    val supplierPhonesCsv: String = "" // key:phone,key:phone
)
