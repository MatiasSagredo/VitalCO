package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Compras
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.ComprasDao

class ComprasRepositoryImpl(
    private val comprasDao: ComprasDao,
    private val apiService: ApiService
) : ComprasRepository {

    override suspend fun getCompras(): List<Compras> {
        val localCompras = comprasDao.getAllCompras()
        
        try {
            val response = apiService.getCompras()
            if (response.isSuccessful) {
                response.body()?.let { compras ->
                    comprasDao.deleteAllCompras()
                    comprasDao.insertCompras(compras)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localCompras
    }

    override suspend fun getCompraById(id: Int): Compras? {
        return try {
            val response = apiService.getCompraById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    comprasDao.insertCompra(it)
                    it
                } ?: comprasDao.getCompraById(id)
            } else {
                comprasDao.getCompraById(id)
            }
        } catch (e: Exception) {
            comprasDao.getCompraById(id)
        }
    }

    override suspend fun addCompra(compra: Compras) {
        try {
            val response = apiService.createCompras(compra)
            if (response.isSuccessful) {
                response.body()?.let { comprasDao.insertCompra(it) }
            } else {
                comprasDao.insertCompra(compra)
            }
        } catch (e: Exception) {
            comprasDao.insertCompra(compra)
        }
    }

    override suspend fun updateCompra(compra: Compras) {
        try {
            val response = apiService.updateCompras(compra.id, compra)
            if (response.isSuccessful) {
                response.body()?.let { comprasDao.updateCompra(it) }
            } else {
                comprasDao.updateCompra(compra)
            }
        } catch (e: Exception) {
            comprasDao.updateCompra(compra)
        }
    }

    override suspend fun deleteCompra(compra: Compras) {
        try {
            apiService.deleteCompras(compra.id)
        } catch (e: Exception) {
        }
        comprasDao.deleteCompra(compra)
    }
}


