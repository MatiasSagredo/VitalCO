package com.example.vitalco.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.model.User
import com.example.vitalco.data.repository.UserRepositoryImpl

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepositoryImpl(
        AppDatabase.getDatabase(application).userDao()
    )

    suspend fun login(username: String, password: String): Result<User> {
        return userRepository.login(username, password)
    }

    suspend fun register(username: String, email: String, password: String): Result<User> {
        return userRepository.register(username, email, password)
    }
}
