package com.example.vitalco.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vitalco.data.model.*
import com.example.vitalco.data.remote.dao.*

@Database(
        entities =
                [
                        Usuarios::class,
                        Productos::class,
                        MovimientosStock::class
                ],
        version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UsuariosDao
    abstract fun productDao(): ProductosDao
    abstract fun stockMovementsDao(): MovimientosStockDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        Room.databaseBuilder(
                                        context.applicationContext,
                                        AppDatabase::class.java,
                                        "vitalco_db"
                                )
                                .fallbackToDestructiveMigration()
                                .build()
                                .also { INSTANCE = it }
                    }
        }
    }
}

