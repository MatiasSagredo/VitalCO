package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Productos

interface ProductosRepository {
    suspend fun addProductos(productos: Productos)
    suspend fun updateProductos(productos: Productos)
    suspend fun deleteProductos(productos: Productos)
    suspend fun getProductosById(id: Int): Productos?
    suspend fun getAllProductos(): List<Productos>
    suspend fun getLowStockProductos(): List<Productos>
    suspend fun searchProductos(query: String): List<Productos>
}

