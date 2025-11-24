package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.UsuariosDao

class UsuariosRepositoryImpl(
    private val usuariosDao: UsuariosDao,
    private val apiService: ApiService
) : UsuariosRepository {

    override suspend fun login(username: String, password: String): Result<Usuarios> {
        return try {
            if (username.isBlank() || password.isBlank()) {
                throw Exception("Por favor completa todos los campos")
            }

            try {
                val response = apiService.getUsuarioById(username.toIntOrNull() ?: 1)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        if (user.password == password) {
                            usuariosDao.insertUser(user)
                            return Result.success(user)
                        }
                    }
                }
            } catch (e: Exception) {
            }

            val user = usuariosDao.getUserByEmail(username)
                ?: throw Exception("El usuario '$username' no existe")

            if (user.password != password) {
                throw Exception("Contraseña incorrecta para '$username'")
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Usuarios> {
        return try {
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                throw Exception("Todos los campos son obligatorios")
            }

            if (password.length < 6) {
                throw Exception("La contraseña debe tener mínimo 6 caracteres")
            }

            val existingEmail = usuariosDao.getUserByEmail(email)
            if (existingEmail != null) {
                throw Exception("El email '$email' ya está en uso")
            }

            val fecha = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            val newUser = Usuarios(id = null, nombre = username, email = email, password = password, rol = "user", creadoEn = fecha)

            try {
                val response = apiService.createUsuarios(newUser)
                if (response.isSuccessful) {
                    response.body()?.let { savedUser ->
                        usuariosDao.insertUser(savedUser)
                        return Result.success(savedUser)
                    }
                }
            } catch (e: Exception) {
            }

            usuariosDao.insertUser(newUser)
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Usuarios? {
        return usuariosDao.getUserByEmail(email)
    }
}


