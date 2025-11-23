package com.example.vitalco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.session.SessionManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    
    val currentUser: StateFlow<Usuarios?> = sessionManager.currentUser

    private val _selectedProduct = MutableStateFlow<Productos?>(null)
    val selectedProduct: StateFlow<Productos?> = _selectedProduct.asStateFlow()

    fun setCurrentUser(user: Usuarios) {
        sessionManager.saveUser(user)
    }

    fun logout() {
        sessionManager.logout()
    }

    fun setSelectedProduct(product: Productos) {
        _selectedProduct.value = product
    }
}

