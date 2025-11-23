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
                val response = apiService.login(com.example.vitalco.data.remote.LoginRequest(username, password))
                if (response.isSuccessful) {
                    response.body()?.user?.let { user ->
                        usuariosDao.insertUser(user)
                        return Result.success(user)
                    }
                }
            } catch (e: Exception) {
            }

            val user = usuariosDao.getUserByUsername(username)
                ?: throw Exception("El Usuarios '$username' no existe")

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

            val existingUser = usuariosDao.getUserByUsername(username)
            if (existingUser != null) {
                throw Exception("El Usuarios '$username' ya está registrado")
            }

            val existingEmail = usuariosDao.getUserByEmail(email)
            if (existingEmail != null) {
                throw Exception("El email '$email' ya está en uso")
            }

            try {
                val response = apiService.register(com.example.vitalco.data.remote.RegisterRequest(username, email, password))
                if (response.isSuccessful) {
                    response.body()?.let { newUser ->
                        usuariosDao.insertUser(newUser)
                        return Result.success(newUser)
                    }
                }
            } catch (e: Exception) {
            }

            val newUser = Usuarios(nombre = username, email = email, password = password, rol = "Usuarios", creadoEn = System.currentTimeMillis())
            usuariosDao.insertUser(newUser)

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByUsername(username: String): Usuarios? {
        return usuariosDao.getUserByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): Usuarios? {
        return usuariosDao.getUserByEmail(email)
    }
}


