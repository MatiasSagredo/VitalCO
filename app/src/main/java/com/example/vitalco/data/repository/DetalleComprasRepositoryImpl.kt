package com.example.vitalco.data.repository

import com.example.vitalco.data.model.DetalleCompras
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.DetalleComprasDao

class DetalleComprasRepositoryImpl(
    private val detalleComprasDao: DetalleComprasDao,
    private val apiService: ApiService
) : DetalleComprasRepository {

    override suspend fun getDetalleCompras(): List<DetalleCompras> {
        val localDetalleCompras = detalleComprasDao.getAllDetalleCompras()
        
        try {
            val response = apiService.getDetalleCompras()
            if (response.isSuccessful) {
                response.body()?.let { detalleCompras ->
                    detalleComprasDao.deleteAllDetalleCompras()
                    detalleComprasDao.insertDetalleCompras(detalleCompras)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localDetalleCompras
    }

    override suspend fun getDetalleCompraById(id: Int): DetalleCompras? {
        return try {
            val response = apiService.getDetalleComprasById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    detalleComprasDao.insertDetalleCompra(it)
                    it
                } ?: detalleComprasDao.getDetalleCompraById(id)
            } else {
                detalleComprasDao.getDetalleCompraById(id)
            }
        } catch (e: Exception) {
            detalleComprasDao.getDetalleCompraById(id)
        }
    }

    override suspend fun addDetalleCompra(detalleCompra: DetalleCompras) {
        try {
            val response = apiService.createDetalleCompras(detalleCompra)
            if (response.isSuccessful) {
                response.body()?.let { detalleComprasDao.insertDetalleCompra(it) }
            } else {
                detalleComprasDao.insertDetalleCompra(detalleCompra)
            }
        } catch (e: Exception) {
            detalleComprasDao.insertDetalleCompra(detalleCompra)
        }
    }

    override suspend fun updateDetalleCompra(detalleCompra: DetalleCompras) {
        try {
            val response = apiService.updateDetalleCompras(detalleCompra.id, detalleCompra)
            if (response.isSuccessful) {
                response.body()?.let { detalleComprasDao.updateDetalleCompra(it) }
            } else {
                detalleComprasDao.updateDetalleCompra(detalleCompra)
            }
        } catch (e: Exception) {
            detalleComprasDao.updateDetalleCompra(detalleCompra)
        }
    }

    override suspend fun deleteDetalleCompra(detalleCompra: DetalleCompras) {
        try {
            apiService.deleteDetalleCompras(detalleCompra.id)
        } catch (e: Exception) {
        }
        detalleComprasDao.deleteDetalleCompra(detalleCompra)
    }
}


