package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Usuarios

interface UsuariosRepository {
    suspend fun login(nombreUsuario: String, contrasena: String): Result<Usuarios>
    suspend fun register(nombreUsuario: String, correo: String, contrasena: String): Result<Usuarios>
    suspend fun getUserByEmail(email: String): Usuarios?
    suspend fun updateUser(user: Usuarios): Result<Usuarios>
    suspend fun uploadImage(file: java.io.File): String?
    suspend fun refreshUserById(id: Int): Usuarios?
}
