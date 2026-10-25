package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ReceivingInspectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceivingDao {
    @Query("SELECT * FROM receiving_inspections ORDER BY createdAt DESC")
    fun getAllInspections(): Flow<List<ReceivingInspectionEntity>>

    @Query("SELECT * FROM receiving_inspections WHERE orderId = :orderId ORDER BY createdAt DESC")
    fun getInspectionsForOrder(orderId: String): Flow<List<ReceivingInspectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: ReceivingInspectionEntity)
}
