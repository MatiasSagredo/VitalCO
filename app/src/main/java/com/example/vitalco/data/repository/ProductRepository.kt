package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun getProductById(id: Int): Product?
    suspend fun getProductBySku(sku: String): Product?
    fun getAllProducts(): Flow<List<Product>>
    fun getLowStockProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
}

