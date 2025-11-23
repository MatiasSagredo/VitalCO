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

class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository = ProductosRepositoryImpl(
        AppDatabase.getDatabase(application).productDao(),
        RetrofitInstance.apiService
    )

    private val _productos = MutableStateFlow<List<Productos>>(emptyList())
    val productos: StateFlow<List<Productos>> = _productos.asStateFlow()

    private val _lowStockProductos = MutableStateFlow<List<Productos>>(emptyList())
    val lowStockProductos: StateFlow<List<Productos>> = _lowStockProductos.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Productos>>(emptyList())
    val searchResults: StateFlow<List<Productos>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val productosFlow = flow {
        try {
            val productos = withContext(Dispatchers.IO) {
                productRepository.getAllProductos()
            }
            emit(productos)
        } catch (e: Exception) {
            throw e
        }
    }

    val lowStockProductosFlow = flow {
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
        // Carga será triggered por LaunchedEffect en ProductScreen
    }

    fun loadAllProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                val productos = productRepository.getAllProductos()
                withContext(Dispatchers.Main) {
                    _productos.value = productos
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

    fun loadLowStockProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productos = productRepository.getLowStockProductos()
                withContext(Dispatchers.Main) {
                    _lowStockProductos.value = productos
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al cargar productos de bajo stock"
                }
                e.printStackTrace()
            }
        }
    }

    fun searchProductos(query: String) {
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

    fun addProducto(productos: Productos) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                productRepository.addProductos(productos)
                loadAllProductos()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al agregar producto"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun updateProductosStock(productos: Productos, newStock: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                val updated = productos.copy(stock_actual = newStock)
                productRepository.updateProductos(updated)
                loadAllProductos()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al actualizar stock"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun deleteProductos(productos: Productos) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _error.value = null
                }
                productRepository.deleteProductos(productos)
                loadAllProductos()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al eliminar producto"
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
