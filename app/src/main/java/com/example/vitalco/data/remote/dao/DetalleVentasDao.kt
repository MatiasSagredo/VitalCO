package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.DetalleVentas

@Dao
interface DetalleVentasDao {

    @Query("SELECT * FROM detalle_ventas")
    suspend fun getAllDetalleVentas(): List<DetalleVentas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalleVenta(detalleVenta: DetalleVentas): Long

    @Update suspend fun updateDetalleVenta(detalleVenta: DetalleVentas)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalleVentas(detalleVentas: List<DetalleVentas>)

    @Query("SELECT * FROM detalle_ventas WHERE id = :id LIMIT 1")
    suspend fun getDetalleVentaById(id: Int): DetalleVentas?

    @Query("SELECT * FROM detalle_ventas WHERE idVenta = :ventaId ORDER BY id ASC")
    suspend fun getDetallesByVenta(ventaId: Int): List<DetalleVentas>

    @Query("SELECT * FROM detalle_ventas WHERE idProducto = :productosId ORDER BY idVenta DESC")
    suspend fun getDetallesByProductos(productosId: Int): List<DetalleVentas>

    @Query("SELECT SUM(subtotal) FROM detalle_ventas WHERE idVenta = :ventaId")
    suspend fun getTotalForVenta(ventaId: Int): Double?

    @Delete suspend fun deleteDetalleVenta(detalleVenta: DetalleVentas)

    @Query("DELETE FROM detalle_ventas WHERE idVenta = :ventaId")
    suspend fun deleteDetallesByVenta(ventaId: Int)

    @Query("DELETE FROM detalle_ventas") suspend fun deleteAllDetalleVentas()

    @Transaction
    suspend fun upsertDetalleVenta(detalleVenta: DetalleVentas) {
        val existing = getDetalleVentaById(detalleVenta.id)
        if (existing != null) {
            updateDetalleVenta(detalleVenta)
        } else {
            insertDetalleVenta(detalleVenta)
        }
    }
}

