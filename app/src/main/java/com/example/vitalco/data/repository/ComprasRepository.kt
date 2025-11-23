package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Compras

interface ComprasRepository {
    suspend fun getCompras(): List<Compras>
    suspend fun getCompraById(id: Int): Compras?
    suspend fun addCompra(Compras: Compras)
    suspend fun updateCompra(Compras: Compras)
    suspend fun deleteCompra(Compras: Compras)
}

