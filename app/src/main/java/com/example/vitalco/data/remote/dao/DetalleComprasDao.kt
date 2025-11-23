package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.DetalleCompras

@Dao
interface DetalleComprasDao {
    @Query("SELECT * FROM detalle_compras")
    suspend fun getAllDetalleCompras(): List<DetalleCompras>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalleCompra(detalleCompra: DetalleCompras): Long

    @Update suspend fun updateDetalleCompra(detalleCompra: DetalleCompras)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalleCompras(detalleCompras: List<DetalleCompras>)

    @Query("SELECT * FROM detalle_compras WHERE id = :id LIMIT 1")
    suspend fun getDetalleCompraById(id: Int): DetalleCompras?

    @Query("SELECT * FROM detalle_compras WHERE idCompra = :compraId ORDER BY id ASC")
    suspend fun getDetallesByCompra(compraId: Int): List<DetalleCompras>

    @Query("SELECT * FROM detalle_compras WHERE idProducto = :productosId ORDER BY idCompra DESC")
    suspend fun getDetallesByProductos(productosId: Int): List<DetalleCompras>

    @Query("SELECT SUM(subtotal) FROM detalle_compras WHERE idCompra = :compraId")
    suspend fun getTotalForCompra(compraId: Int): Double?

    @Delete suspend fun deleteDetalleCompra(detalleCompra: DetalleCompras)

    @Query("DELETE FROM detalle_compras WHERE idCompra = :compraId")
    suspend fun deleteDetallesByCompra(compraId: Int)

    @Query("DELETE FROM detalle_compras") suspend fun deleteAllDetalleCompras()

    @Transaction
    suspend fun upsertDetalleCompra(detalleCompra: DetalleCompras) {
        val existing = getDetalleCompraById(detalleCompra.id)
        if (existing != null) {
            updateDetalleCompra(detalleCompra)
        } else {
            insertDetalleCompra(detalleCompra)
        }
    }
}

