package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Clientes
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.ClientesDao

class ClientesRepositoryImpl(
    private val clientesDao: ClientesDao,
    private val apiService: ApiService
) : ClientesRepository {

    override suspend fun getClientes(): List<Clientes> {
        val localClientes = clientesDao.getAllClientes()
        
        try {
            val response = apiService.getClientes()
            if (response.isSuccessful) {
                response.body()?.let { clientes ->
                    clientesDao.deleteAllClientes()
                    clientesDao.insertClientes(clientes)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localClientes
    }

    override suspend fun getClienteById(id: Int): Clientes? {
        return try {
            val response = apiService.getClienteById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    clientesDao.insertCliente(it)
                    it
                } ?: clientesDao.getClienteById(id)
            } else {
                clientesDao.getClienteById(id)
            }
        } catch (e: Exception) {
            clientesDao.getClienteById(id)
        }
    }

    override suspend fun addCliente(cliente: Clientes) {
        try {
            val response = apiService.createClientes(cliente)
            if (response.isSuccessful) {
                response.body()?.let { clientesDao.insertCliente(it) }
            } else {
                clientesDao.insertCliente(cliente)
            }
        } catch (e: Exception) {
            clientesDao.insertCliente(cliente)
        }
    }

    override suspend fun updateCliente(cliente: Clientes) {
        try {
            val response = apiService.updateClientes(cliente.id, cliente)
            if (response.isSuccessful) {
                response.body()?.let { clientesDao.updateCliente(it) }
            } else {
                clientesDao.updateCliente(cliente)
            }
        } catch (e: Exception) {
            clientesDao.updateCliente(cliente)
        }
    }

    override suspend fun deleteCliente(cliente: Clientes) {
        try {
            apiService.deleteClientes(cliente.id)
        } catch (e: Exception) {
        }
        clientesDao.deleteCliente(cliente)
    }
}


