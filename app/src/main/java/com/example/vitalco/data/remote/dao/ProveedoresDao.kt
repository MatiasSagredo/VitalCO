package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Proveedores
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProveedor(proveedor: Proveedores): Long

    @Update suspend fun updateProveedor(proveedor: Proveedores)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProveedores(proveedores: List<Proveedores>)

    @Query("SELECT * FROM proveedores WHERE id = :id LIMIT 1")
    suspend fun getProveedorById(id: Int): Proveedores?

    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    suspend fun getAllProveedores(): List<Proveedores>

    @Query(
            "SELECT * FROM proveedores WHERE nombre LIKE '%' || :query || '%' OR telefono LIKE '%' || :query || '%' ORDER BY nombre ASC"
    )
    suspend fun searchProveedores(query: String): List<Proveedores>

    @Delete suspend fun deleteProveedor(proveedor: Proveedores)

    @Query("DELETE FROM proveedores WHERE id = :id") suspend fun deleteProveedorById(id: Int)

    @Query("DELETE FROM proveedores") suspend fun deleteAllProveedores()

    @Transaction
    suspend fun upsertProveedor(proveedor: Proveedores) {
        val existing = getProveedorById(proveedor.id)
        if (existing != null) {
            updateProveedor(proveedor)
        } else {
            insertProveedor(proveedor)
        }
    }
}

