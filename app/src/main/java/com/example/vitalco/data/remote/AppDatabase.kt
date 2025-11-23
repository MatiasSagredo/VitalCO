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
                        Clientes::class,
                        Compras::class,
                        DetalleCompras::class,
                        Ventas::class,
                        DetalleVentas::class,
                        MovimientosStock::class,
                        Proveedores::class],
        version = 6
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UsuariosDao
    abstract fun productDao(): ProductosDao
    abstract fun salesDao(): VentasDao
    abstract fun clientsDao(): ClientesDao
    abstract fun purchasesDao(): ComprasDao
    abstract fun purchaseDetailsDao(): DetalleComprasDao
    abstract fun saleDetailsDao(): DetalleVentasDao
    abstract fun stockMovementsDao(): MovimientosStockDao
    abstract fun suppliersDao(): ProveedoresDao

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

