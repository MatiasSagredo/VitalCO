package com.example.vitalco.data.repository

import com.example.vitalco.data.model.DetalleVentas
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.DetalleVentasDao

class DetalleVentasRepositoryImpl(
    private val detalleVentasDao: DetalleVentasDao,
    private val apiService: ApiService
) : DetalleVentasRepository {

    override suspend fun getDetalleVentas(): List<DetalleVentas> {
        val localDetalleVentas = detalleVentasDao.getAllDetalleVentas()
        
        try {
            val response = apiService.getDetalleVentas()
            if (response.isSuccessful) {
                response.body()?.let { detalleVentas ->
                    detalleVentasDao.deleteAllDetalleVentas()
                    detalleVentasDao.insertDetalleVentas(detalleVentas)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localDetalleVentas
    }

    override suspend fun getDetalleVentaById(id: Int): DetalleVentas? {
        return try {
            val response = apiService.getDetalleVentasById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    detalleVentasDao.insertDetalleVenta(it)
                    it
                } ?: detalleVentasDao.getDetalleVentaById(id)
            } else {
                detalleVentasDao.getDetalleVentaById(id)
            }
        } catch (e: Exception) {
            detalleVentasDao.getDetalleVentaById(id)
        }
    }

    override suspend fun addDetalleVenta(detalleVenta: DetalleVentas) {
        try {
            val response = apiService.createDetalleVentas(detalleVenta)
            if (response.isSuccessful) {
                response.body()?.let { detalleVentasDao.insertDetalleVenta(it) }
            } else {
                detalleVentasDao.insertDetalleVenta(detalleVenta)
            }
        } catch (e: Exception) {
            detalleVentasDao.insertDetalleVenta(detalleVenta)
        }
    }

    override suspend fun updateDetalleVenta(detalleVenta: DetalleVentas) {
        try {
            val response = apiService.updateDetalleVentas(detalleVenta.id, detalleVenta)
            if (response.isSuccessful) {
                response.body()?.let { detalleVentasDao.updateDetalleVenta(it) }
            } else {
                detalleVentasDao.updateDetalleVenta(detalleVenta)
            }
        } catch (e: Exception) {
            detalleVentasDao.updateDetalleVenta(detalleVenta)
        }
    }

    override suspend fun deleteDetalleVenta(detalleVenta: DetalleVentas) {
        try {
            apiService.deleteDetalleVentas(detalleVenta.id)
        } catch (e: Exception) {
        }
        detalleVentasDao.deleteDetalleVenta(detalleVenta)
    }
}


