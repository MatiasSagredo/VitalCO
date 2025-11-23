package com.example.vitalco.data.repository

import com.example.vitalco.data.model.DetalleCompras

interface DetalleComprasRepository {
    suspend fun getDetalleCompras(): List<DetalleCompras>
    suspend fun getDetalleCompraById(id: Int): DetalleCompras?
    suspend fun addDetalleCompra(detalleCompra: DetalleCompras)
    suspend fun updateDetalleCompra(detalleCompra: DetalleCompras)
    suspend fun deleteDetalleCompra(detalleCompra: DetalleCompras)
}

