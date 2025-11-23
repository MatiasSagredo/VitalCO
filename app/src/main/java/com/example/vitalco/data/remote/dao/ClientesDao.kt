package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Clientes
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: Clientes): Long

    @Update suspend fun updateCliente(cliente: Clientes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClientes(clientes: List<Clientes>)

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun getClienteById(id: Int): Clientes?

    @Query("SELECT * FROM clientes ORDER BY nombre ASC")
    suspend fun getAllClientes(): List<Clientes>

    @Query(
            "SELECT * FROM clientes WHERE nombre LIKE '%' || :query || '%' OR telefono LIKE '%' || :query || '%' ORDER BY nombre ASC"
    )
    suspend fun searchClientes(query: String): List<Clientes>

    @Delete suspend fun deleteCliente(cliente: Clientes)

    @Query("DELETE FROM clientes WHERE id = :id") suspend fun deleteClienteById(id: Int)

    @Query("DELETE FROM clientes") suspend fun deleteAllClientes()

    @Transaction
    suspend fun upsertCliente(cliente: Clientes) {
        val existing = getClienteById(cliente.id)
        if (existing != null) {
            updateCliente(cliente)
        } else {
            insertCliente(cliente)
        }
    }
}

