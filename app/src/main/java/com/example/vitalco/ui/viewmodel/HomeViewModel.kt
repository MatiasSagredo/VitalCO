package com.example.vitalco.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = AppDatabase.getDatabase(application).productDao()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _lowStockProducts = MutableStateFlow<List<Product>>(emptyList())
    val lowStockProducts: StateFlow<List<Product>> = _lowStockProducts.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllProducts()
        loadLowStockProducts()
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collect { products ->
                _products.value = products
            }
        }
    }

    fun loadLowStockProducts() {
        viewModelScope.launch {
            productDao.getLowStockProducts().collect { products ->
                _lowStockProducts.value = products
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            productDao.searchProducts(query).collect { products ->
                _searchResults.value = products
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                productDao.addProduct(product)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProductStock(product: Product, newStock: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val updated = product.copy(currentStock = newStock)
                productDao.updateProduct(updated)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productDao.deleteProduct(product)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
