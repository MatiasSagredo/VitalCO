package com.example.vitalco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.RetrofitInstance
import com.example.vitalco.data.repository.ProductosRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository = ProductosRepositoryImpl(
        AppDatabase.getDatabase(application).productDao(),
        RetrofitInstance.apiService
    )

    private val _products = MutableStateFlow<List<Productos>>(emptyList())
    val products: StateFlow<List<Productos>> = _products.asStateFlow()

    private val _lowStockProducts = MutableStateFlow<List<Productos>>(emptyList())
    val lowStockProducts: StateFlow<List<Productos>> = _lowStockProducts.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Productos>>(emptyList())
    val searchResults: StateFlow<List<Productos>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val productsFlow = flow {
        try {
            val productos = withContext(Dispatchers.IO) {
                productRepository.getAllProductos()
            }
            emit(productos)
        } catch (e: Exception) {
            throw e
        }
    }

    val lowStockProductsFlow = flow {
        try {
            val productos = withContext(Dispatchers.IO) {
                productRepository.getLowStockProductos()
            }
            emit(productos)
        } catch (e: Exception) {
            throw e
        }
    }

    init {
        loadAllProducts()
        loadLowStockProducts()
    }

    fun loadAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                val productos = productRepository.getAllProductos()
                withContext(Dispatchers.Main) {
                    _products.value = productos
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al cargar productos"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun loadLowStockProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productos = productRepository.getLowStockProductos()
                withContext(Dispatchers.Main) {
                    _lowStockProducts.value = productos
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al cargar productos de bajo stock"
                }
                e.printStackTrace()
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                }
                val productos = productRepository.searchProductos(query)
                withContext(Dispatchers.Main) {
                    _searchResults.value = productos
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error en búsqueda"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun addProduct(Productos: Productos) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                productRepository.addProductos(Productos)
                loadAllProducts()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al agregar Productos"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun updateProductosStock(Productos: Productos, newStock: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                val updated = Productos.copy(stock_actual = newStock)
                productRepository.updateProductos(updated)
                loadAllProducts()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al actualizar stock"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun deleteProductos(Productos: Productos) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                productRepository.deleteProductos(Productos)
                loadAllProducts()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al eliminar Productos"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

