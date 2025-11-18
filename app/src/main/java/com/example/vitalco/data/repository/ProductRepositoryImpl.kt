package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.dao.ProductDao
import com.example.vitalco.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }

    override suspend fun getProductBySku(sku: String): Product? {
        return productDao.getProductBySku(sku)
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    override fun getLowStockProducts(): Flow<List<Product>> {
        return productDao.getLowStockProducts()
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }
}
