package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SystemSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM system_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettings(key: String): SystemSettingsEntity?

    @Query("SELECT * FROM system_settings WHERE `key` = :key LIMIT 1")
    fun observeSettings(key: String): Flow<SystemSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SystemSettingsEntity)
}
