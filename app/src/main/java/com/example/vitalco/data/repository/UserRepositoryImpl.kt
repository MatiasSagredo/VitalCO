package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.dao.UserDao
import com.example.vitalco.data.model.User

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(username: String, password: String): Result<User> {
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

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<User> {
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

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
