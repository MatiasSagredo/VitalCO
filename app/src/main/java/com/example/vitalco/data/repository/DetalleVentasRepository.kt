package com.example.vitalco.data.repository

import com.example.vitalco.data.model.DetalleVentas

interface DetalleVentasRepository {
    suspend fun getDetalleVentas(): List<DetalleVentas>
    suspend fun getDetalleVentaById(id: Int): DetalleVentas?
    suspend fun addDetalleVenta(detalleVenta: DetalleVentas)
    suspend fun updateDetalleVenta(detalleVenta: DetalleVentas)
    suspend fun deleteDetalleVenta(detalleVenta: DetalleVentas)
}

