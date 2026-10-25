package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val employeeNumber: String,
    val name: String,
    val role: String, // "manager", "assistant", "receiver"
    val pin: String = "1234",
    val phone: String = "",
    val assignedCategoriesCsv: String = "",
    val isActive: Boolean = true
)
