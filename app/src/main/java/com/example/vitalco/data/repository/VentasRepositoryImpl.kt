package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Ventas
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.VentasDao

class VentasRepositoryImpl(
    private val ventasDao: VentasDao,
    private val apiService: ApiService
) : VentasRepository {

    override suspend fun getVentas(): List<Ventas> {
        val localVentas = ventasDao.getAllVentas()
        
        try {
            val response = apiService.getVentas()
            if (response.isSuccessful) {
                response.body()?.let { ventas ->
                    ventasDao.deleteAllVentas()
                    ventasDao.insertVentas(ventas)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localVentas
    }

    override suspend fun getVentaById(id: Int): Ventas? {
        return try {
            val response = apiService.getVentaById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    ventasDao.insertVenta(it)
                    it
                } ?: ventasDao.getVentaById(id)
            } else {
                ventasDao.getVentaById(id)
            }
        } catch (e: Exception) {
            ventasDao.getVentaById(id)
        }
    }

    override suspend fun addVenta(venta: Ventas) {
        try {
            val response = apiService.createVentas(venta)
            if (response.isSuccessful) {
                response.body()?.let { ventasDao.insertVenta(it) }
            } else {
                ventasDao.insertVenta(venta)
            }
        } catch (e: Exception) {
            ventasDao.insertVenta(venta)
        }
    }

    override suspend fun updateVenta(venta: Ventas) {
        try {
            val response = apiService.updateVentas(venta.id, venta)
            if (response.isSuccessful) {
                response.body()?.let { ventasDao.updateVenta(it) }
            } else {
                ventasDao.updateVenta(venta)
            }
        } catch (e: Exception) {
            ventasDao.updateVenta(venta)
        }
    }

    override suspend fun deleteVenta(venta: Ventas) {
        try {
            apiService.deleteVentas(venta.id)
        } catch (e: Exception) {
        }
        ventasDao.deleteVenta(venta)
    }
}


