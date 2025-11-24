package com.example.vitalco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalco.data.model.MovimientosStock
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.RetrofitInstance
import com.example.vitalco.data.repository.MovimientosStockRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientosStockViewModel(application: Application) : AndroidViewModel(application) {
    private val movimientosRepository = MovimientosStockRepositoryImpl(
        AppDatabase.getDatabase(application).stockMovementsDao(),
        RetrofitInstance.apiService
    )

    private val _movimientos = MutableStateFlow<List<MovimientosStock>>(emptyList())
    val movimientos: StateFlow<List<MovimientosStock>> = _movimientos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMovimientos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                val movimientos = movimientosRepository.getMovimientosStock()
                withContext(Dispatchers.Main) {
                    _movimientos.value = movimientos.sortedByDescending { it.fecha }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al cargar movimientos"
                    _isLoading.value = false
                }
                e.printStackTrace()
            }
        }
    }

    fun addMovimiento(idProducto: Int, tipoMovimiento: String, cantidad: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val movimiento = MovimientosStock(
                    id = null,
                    idProducto = idProducto,
                    tipoMovimiento = tipoMovimiento,
                    cantidad = cantidad,
                    fecha = fechaActual
                )
                movimientosRepository.addMovimientoStock(movimiento)
                loadMovimientos()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message ?: "Error al registrar movimiento"
                }
                e.printStackTrace()
            }
        }
    }

    fun getMovimientosByProducto(idProducto: Int): List<MovimientosStock> {
        return _movimientos.value.filter { it.idProducto == idProducto }
    }

    fun clearError() {
        _error.value = null
    }
}
