package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.ProductosDao

class ProductosRepositoryImpl(
    private val productosDao: ProductosDao,
    private val apiService: ApiService
) : ProductosRepository {

    override suspend fun addProductos(productos: Productos) {
        try {
            val response = apiService.createProductos(productos)
            if (response.isSuccessful) {
                response.body()?.let { productosDao.insertProduct(it) }
            } else {
                productosDao.insertProduct(productos)
            }
        } catch (e: Exception) {
            productosDao.insertProduct(productos)
        }
    }

    override suspend fun updateProductos(productos: Productos) {
        try {
            val response = apiService.updateProductos(productos.id, productos)
            if (response.isSuccessful) {
                response.body()?.let { productosDao.updateProductos(it) }
            } else {
                productosDao.updateProductos(productos)
            }
        } catch (e: Exception) {
            productosDao.updateProductos(productos)
        }
    }

    override suspend fun deleteProductos(productos: Productos) {
        try {
            apiService.deleteProductos(productos.id)
        } catch (e: Exception) {
        }
        productosDao.deleteProductos(productos)
    }

    override suspend fun getProductosById(id: Int): Productos? {
        return try {
            val response = apiService.getProductoById(id)
            if (response.isSuccessful) {
                response.body()?.let { 
                    productosDao.insertProduct(it)
                    it
                } ?: productosDao.getProductoById(id)
            } else {
                productosDao.getProductoById(id)
            }
        } catch (e: Exception) {
            productosDao.getProductoById(id)
        }
    }

    override suspend fun getAllProductos(): List<Productos> {
        val localProducts = productosDao.getAllProducts()
        
        try {
            val response = apiService.getProductos()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    productosDao.deleteAllProducts()
                    productosDao.insertProducts(products)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return localProducts
    }

    override suspend fun getLowStockProductos(): List<Productos> {
        return productosDao.getLowStockProducts()
    }

    override suspend fun searchProductos(query: String): List<Productos> {
        return productosDao.searchProducts(query)
    }
}


