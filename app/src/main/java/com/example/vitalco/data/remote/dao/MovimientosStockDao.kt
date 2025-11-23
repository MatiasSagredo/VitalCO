package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.MovimientosStock

@Dao
interface MovimientosStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimiento(movimiento: MovimientosStock): Long

    @Update suspend fun updateMovimiento(movimiento: MovimientosStock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimientos(movimientos: List<MovimientosStock>)

    @Query("SELECT * FROM movimientos_stock WHERE id = :id LIMIT 1")
    suspend fun getMovimientoById(id: Int): MovimientosStock?

    @Query("SELECT * FROM movimientos_stock ORDER BY fecha DESC")
    suspend fun getAllMovimientos(): List<MovimientosStock>

    @Query("SELECT * FROM movimientos_stock WHERE idProducto = :productosId ORDER BY fecha DESC")
    suspend fun getMovimientosByProductos(productosId: Int): List<MovimientosStock>

    @Query("SELECT * FROM movimientos_stock WHERE tipoMovimiento = :type ORDER BY fecha DESC")
    suspend fun getMovimientosByType(type: String): List<MovimientosStock>

    @Query(
            "SELECT SUM(cantidad) FROM movimientos_stock WHERE idProducto = :productosId AND tipoMovimiento = 'ENTRADA'"
    )
    suspend fun getTotalEntradasForProductos(productosId: Int): Int?

    @Query(
            "SELECT SUM(cantidad) FROM movimientos_stock WHERE idProducto = :productosId AND tipoMovimiento = 'SALIDA'"
    )
    suspend fun getTotalSalidasForProductos(productosId: Int): Int?

    @Delete suspend fun deleteMovimiento(movimiento: MovimientosStock)

    @Query("DELETE FROM movimientos_stock WHERE idProducto = :productosId")
    suspend fun deleteMovimientosByProductos(productosId: Int)

    @Query("DELETE FROM movimientos_stock") suspend fun deleteAllMovimientos()

    @Transaction
    suspend fun upsertMovimiento(movimiento: MovimientosStock) {
        val existing = getMovimientoById(movimiento.id)
        if (existing != null) {
            updateMovimiento(movimiento)
        } else {
            insertMovimiento(movimiento)
        }
    }
}

