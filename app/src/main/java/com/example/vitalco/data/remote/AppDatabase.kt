package com.example.vitalco.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vitalco.data.remote.dao.ProductDao
import com.example.vitalco.data.remote.dao.UserDao
import com.example.vitalco.data.model.Product
import com.example.vitalco.data.model.User

@Database(entities = [User::class, Product::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "vitalco_db"
                            ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
            }
        }
    }
}
