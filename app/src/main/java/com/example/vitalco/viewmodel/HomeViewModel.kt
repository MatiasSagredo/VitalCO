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

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val movimientosRepository = MovimientosStockRepositoryImpl(
        AppDatabase.getDatabase(application).stockMovementsDao(),
        RetrofitInstance.apiService
    )

    private val _ultimoMovimiento = MutableStateFlow<MovimientosStock?>(null)
    val ultimoMovimiento: StateFlow<MovimientosStock?> = _ultimoMovimiento.asStateFlow()

    init {
        loadUltimoMovimiento()
    }

    fun loadUltimoMovimiento() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movimientos = movimientosRepository.getMovimientosStock()
                val ultimo = movimientos.maxByOrNull { it.fecha }
                withContext(Dispatchers.Main) {
                    _ultimoMovimiento.value = ultimo
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
