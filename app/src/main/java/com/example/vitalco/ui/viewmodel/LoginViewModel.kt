package com.example.vitalco.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.remote.model.User

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            if (username.isBlank() || password.isBlank()) {
                throw Exception("Por favor completa todos los campos")
            }

            val user = userDao.getUserByUsername(username)
                ?: throw Exception("El usuario '$username' no existe")

            if (user.password != password) {
                throw Exception("Contraseña incorrecta para '$username'")
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<User> {
        return try {
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                throw Exception("Todos los campos son obligatorios")
            }

            if (password.length < 6) {
                throw Exception("La contraseña debe tener mínimo 6 caracteres")
            }

            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                throw Exception("El usuario '$username' ya está registrado")
            }

            val existingEmail = userDao.getUserByEmail(email)
            if (existingEmail != null) {
                throw Exception("El email '$email' ya está en uso")
            }

            val newUser = User(username = username, email = email, password = password)
            userDao.addUser(newUser)

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
