package com.example.vitalco.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vitalco.data.remote.model.Product
import com.example.vitalco.data.remote.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: Product? get() = _selectedProduct.value

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun setSelectedProduct(product: Product) {
        _selectedProduct.value = product
    }
}