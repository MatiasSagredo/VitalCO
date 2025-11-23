package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Proveedores

interface ProveedoresRepository {
    suspend fun getProveedores(): List<Proveedores>
    suspend fun getProveedorById(id: Int): Proveedores?
    suspend fun addProveedor(Proveedores: Proveedores)
    suspend fun updateProveedor(Proveedores: Proveedores)
    suspend fun deleteProveedor(Proveedores: Proveedores)
}

