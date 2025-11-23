package com.example.vitalco.data.repository

import com.example.vitalco.data.model.MovimientosStock
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.MovimientosStockDao

class MovimientosStockRepositoryImpl(
    private val movimientosStockDao: MovimientosStockDao,
    private val apiService: ApiService
) : MovimientosStockRepository {

    override suspend fun getMovimientosStock(): List<MovimientosStock> {
        val localMovimientos = movimientosStockDao.getAllMovimientos()
        
        try {
            val response = apiService.getMovimientosStock()
            if (response.isSuccessful) {
                response.body()?.let { movimientos ->
                    movimientosStockDao.deleteAllMovimientos()
                    movimientosStockDao.insertMovimientos(movimientos)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localMovimientos
    }

    override suspend fun getMovimientoStockById(id: Int): MovimientosStock? {
        return try {
            val response = apiService.getMovimientosStockById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    movimientosStockDao.insertMovimiento(it)
                    it
                } ?: movimientosStockDao.getMovimientoById(id)
            } else {
                movimientosStockDao.getMovimientoById(id)
            }
        } catch (e: Exception) {
            movimientosStockDao.getMovimientoById(id)
        }
    }

    override suspend fun addMovimientoStock(movimiento: MovimientosStock) {
        try {
            val response = apiService.createMovimientosStock(movimiento)
            if (response.isSuccessful) {
                response.body()?.let { movimientosStockDao.insertMovimiento(it) }
            } else {
                movimientosStockDao.insertMovimiento(movimiento)
            }
        } catch (e: Exception) {
            movimientosStockDao.insertMovimiento(movimiento)
        }
    }

    override suspend fun updateMovimientoStock(movimiento: MovimientosStock) {
        try {
            val response = apiService.updateMovimientosStock(movimiento.id, movimiento)
            if (response.isSuccessful) {
                response.body()?.let { movimientosStockDao.updateMovimiento(it) }
            } else {
                movimientosStockDao.updateMovimiento(movimiento)
            }
        } catch (e: Exception) {
            movimientosStockDao.updateMovimiento(movimiento)
        }
    }

    override suspend fun deleteMovimientoStock(movimiento: MovimientosStock) {
        try {
            apiService.deleteMovimientosStock(movimiento.id)
        } catch (e: Exception) {
        }
        movimientosStockDao.deleteMovimiento(movimiento)
    }
}


