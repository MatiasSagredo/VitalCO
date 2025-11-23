package com.example.vitalco.data.repository

import com.example.vitalco.data.model.MovimientosStock

interface MovimientosStockRepository {
    suspend fun getMovimientosStock(): List<MovimientosStock>
    suspend fun getMovimientoStockById(id: Int): MovimientosStock?
    suspend fun addMovimientoStock(movimiento: MovimientosStock)
    suspend fun updateMovimientoStock(movimiento: MovimientosStock)
    suspend fun deleteMovimientoStock(movimiento: MovimientosStock)
}

