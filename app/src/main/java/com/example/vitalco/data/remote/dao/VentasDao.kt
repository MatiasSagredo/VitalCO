package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Ventas
import kotlinx.coroutines.flow.Flow

@Dao
interface VentasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertVenta(venta: Ventas): Long

    @Update suspend fun updateVenta(venta: Ventas)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertVentas(ventas: List<Ventas>)

    @Query("SELECT * FROM ventas WHERE id = :id LIMIT 1") suspend fun getVentaById(id: Int): Ventas?

    @Query("SELECT * FROM ventas ORDER BY fecha DESC") suspend fun getAllVentas(): List<Ventas>

    @Query("SELECT * FROM ventas WHERE idCliente = :clienteId ORDER BY fecha DESC")
    suspend fun getVentasByCliente(clienteId: Int): List<Ventas>

    @Query("SELECT * FROM ventas WHERE idUsuario = :usuarioId ORDER BY fecha DESC")
    suspend fun getVentasByUsuario(usuarioId: Int): List<Ventas>

    @Query("SELECT SUM(total) FROM ventas WHERE fecha >= :startDate AND fecha <= :endDate")
    suspend fun getTotalVentasBetweenDates(startDate: Long, endDate: Long): Double?

    @Delete suspend fun deleteVenta(venta: Ventas)

    @Query("DELETE FROM ventas WHERE id = :id") suspend fun deleteVentaById(id: Int)

    @Query("DELETE FROM ventas") suspend fun deleteAllVentas()

    @Transaction
    suspend fun upsertVenta(venta: Ventas) {
        val existing = getVentaById(venta.id)
        if (existing != null) {
            updateVenta(venta)
        } else {
            insertVenta(venta)
        }
    }
}

