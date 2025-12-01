package com.example.vitalco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.RetrofitInstance
import com.example.vitalco.data.repository.UsuariosRepositoryImpl
import com.example.vitalco.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    private val usuariosRepository = UsuariosRepositoryImpl(
        AppDatabase.getDatabase(application).userDao(),
        RetrofitInstance.apiService
    )
    
    val currentUser: StateFlow<Usuarios?> = sessionManager.currentUser

    private val _selectedProduct = MutableStateFlow<Productos?>(null)
    val selectedProduct: StateFlow<Productos?> = _selectedProduct.asStateFlow()

    fun setCurrentUser(user: Usuarios) {
        sessionManager.saveUser(user)
    }

    fun updateUserProfile(user: Usuarios) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = usuariosRepository.updateUser(user)
                result.getOrNull()?.let { updatedUser ->
                    sessionManager.saveUser(updatedUser)
                }
            }
        }
    }

    fun refreshCurrentUser() {
        val userId = currentUser.value?.id
        if (userId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val usuarioActualizado = usuariosRepository.refreshUserById(userId)
                    if (usuarioActualizado != null) {
                        sessionManager.saveUser(usuarioActualizado)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun logout() {
        sessionManager.logout()
    }

    fun setSelectedProduct(product: Productos) {
        _selectedProduct.value = product
    }
}

