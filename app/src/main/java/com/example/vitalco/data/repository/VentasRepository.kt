package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Ventas

interface VentasRepository {
    suspend fun getVentas(): List<Ventas>
    suspend fun getVentaById(id: Int): Ventas?
    suspend fun addVenta(Ventas: Ventas)
    suspend fun updateVenta(Ventas: Ventas)
    suspend fun deleteVenta(Ventas: Ventas)
}

