package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Compras
import kotlinx.coroutines.flow.Flow

@Dao
interface ComprasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompra(compra: Compras): Long

    @Update suspend fun updateCompra(compra: Compras)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompras(compras: List<Compras>)

    @Query("SELECT * FROM compras WHERE id = :id LIMIT 1")
    suspend fun getCompraById(id: Int): Compras?

    @Query("SELECT * FROM compras ORDER BY fecha DESC")
    suspend fun getAllCompras(): List<Compras>

    @Query("SELECT * FROM compras WHERE idProveedor = :providerId ORDER BY fecha DESC")
    suspend fun getComprasByProveedor(providerId: Int): List<Compras>

    @Query("SELECT SUM(total) FROM compras WHERE fecha >= :startDate AND fecha <= :endDate")
    suspend fun getTotalComprasBetweenDates(startDate: String, endDate: String): Double?

    @Delete suspend fun deleteCompra(compra: Compras)

    @Query("DELETE FROM compras WHERE id = :id") suspend fun deleteCompraById(id: Int)

    @Query("DELETE FROM compras") suspend fun deleteAllCompras()

    @Transaction
    suspend fun upsertCompra(compra: Compras) {
        val existing = getCompraById(compra.id)
        if (existing != null) {
            updateCompra(compra)
        } else {
            insertCompra(compra)
        }
    }
}

