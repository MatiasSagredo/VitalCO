package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Usuarios

interface UsuariosRepository {
    suspend fun login(username: String, password: String): Result<Usuarios>
    suspend fun register(username: String, email: String, password: String): Result<Usuarios>
    suspend fun getUserByEmail(email: String): Usuarios?
}

