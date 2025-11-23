package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Proveedores
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.ProveedoresDao

class ProveedoresRepositoryImpl(
    private val proveedoresDao: ProveedoresDao,
    private val apiService: ApiService
) : ProveedoresRepository {

    override suspend fun getProveedores(): List<Proveedores> {
        val localProveedores = proveedoresDao.getAllProveedores()
        
        try {
            val response = apiService.getProveedores()
            if (response.isSuccessful) {
                response.body()?.let { proveedores ->
                    proveedoresDao.deleteAllProveedores()
                    proveedoresDao.insertProveedores(proveedores)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localProveedores
    }

    override suspend fun getProveedorById(id: Int): Proveedores? {
        return try {
            val response = apiService.getProveedorById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    proveedoresDao.insertProveedor(it)
                    it
                } ?: proveedoresDao.getProveedorById(id)
            } else {
                proveedoresDao.getProveedorById(id)
            }
        } catch (e: Exception) {
            proveedoresDao.getProveedorById(id)
        }
    }

    override suspend fun addProveedor(proveedor: Proveedores) {
        try {
            val response = apiService.createProveedores(proveedor)
            if (response.isSuccessful) {
                response.body()?.let { proveedoresDao.insertProveedor(it) }
            } else {
                proveedoresDao.insertProveedor(proveedor)
            }
        } catch (e: Exception) {
            proveedoresDao.insertProveedor(proveedor)
        }
    }

    override suspend fun updateProveedor(proveedor: Proveedores) {
        try {
            val response = apiService.updateProveedores(proveedor.id, proveedor)
            if (response.isSuccessful) {
                response.body()?.let { proveedoresDao.updateProveedor(it) }
            } else {
                proveedoresDao.updateProveedor(proveedor)
            }
        } catch (e: Exception) {
            proveedoresDao.updateProveedor(proveedor)
        }
    }

    override suspend fun deleteProveedor(proveedor: Proveedores) {
        try {
            apiService.deleteProveedores(proveedor.id)
        } catch (e: Exception) {
        }
        proveedoresDao.deleteProveedor(proveedor)
    }
}


