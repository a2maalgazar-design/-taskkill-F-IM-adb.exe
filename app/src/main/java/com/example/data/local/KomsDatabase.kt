package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.ReceivingDao
import com.example.data.local.dao.SettingsDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ReceivingInspectionEntity
import com.example.data.local.entity.SystemSettingsEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ReceivingInspectionEntity::class,
        SystemSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KomsDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun receivingDao(): ReceivingDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: KomsDatabase? = null

        fun getDatabase(context: Context): KomsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KomsDatabase::class.java,
                    "koms_kitchen_room_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed data in background coroutine on first create
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateInitialData(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(database: KomsDatabase) {
            val userDao = database.userDao()
            val categoryDao = database.categoryDao()
            val productDao = database.productDao()
            val settingsDao = database.settingsDao()

            if (userDao.getUserCount() == 0) {
                userDao.insertUsers(SeedDataProvider.getInitialUsers())
            }
            if (categoryDao.getCategoryCount() == 0) {
                categoryDao.insertCategories(SeedDataProvider.getInitialCategories())
            }
            if (productDao.getProductCount() == 0) {
                productDao.insertProducts(SeedDataProvider.getInitialProducts())
            }
            if (settingsDao.getSettings("default") == null) {
                settingsDao.saveSettings(SeedDataProvider.getInitialSettings())
            }
        }
    }
}
