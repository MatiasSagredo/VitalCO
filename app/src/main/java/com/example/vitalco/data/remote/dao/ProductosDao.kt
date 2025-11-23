package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Productos
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Productos): Long

    @Update suspend fun updateProductos(product: Productos)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Productos>)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getProductoById(id: Int): Productos?

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    suspend fun getAllProducts(): List<Productos>

    @Query("SELECT * FROM productos WHERE stock_actual <= stock_minimo ORDER BY stock_actual ASC")
    suspend fun getLowStockProducts(): List<Productos>

    @Query(
            "SELECT * FROM productos WHERE nombre LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%' ORDER BY nombre ASC"
    )
    suspend fun searchProducts(query: String): List<Productos>

    @Query("SELECT COUNT(*) FROM productos") suspend fun getProductCount(): Int

    @Query("SELECT * FROM productos WHERE unidad = :unit ORDER BY nombre ASC")
    suspend fun getProductosByUnit(unit: String): List<Productos>

    @Delete suspend fun deleteProductos(product: Productos)

    @Query("DELETE FROM productos WHERE id = :id") suspend fun deleteProductosById(id: Int)

    @Query("DELETE FROM productos") suspend fun deleteAllProducts()

    @Transaction
    suspend fun upsertProduct(product: Productos) {
        val existing = getProductoById(product.id)
        if (existing != null) {
            updateProductos(product)
        } else {
            insertProduct(product)
        }
    }
}

