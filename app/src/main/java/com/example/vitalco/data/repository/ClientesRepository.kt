package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Clientes

interface ClientesRepository {
    suspend fun getClientes(): List<Clientes>
    suspend fun getClienteById(id: Int): Clientes?
    suspend fun addCliente(Clientes: Clientes)
    suspend fun updateCliente(Clientes: Clientes)
    suspend fun deleteCliente(Clientes: Clientes)
}

