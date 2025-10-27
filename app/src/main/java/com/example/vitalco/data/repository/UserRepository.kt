package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.model.User

interface UserRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(username: String, email: String, password: String): Result<User>
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
}

