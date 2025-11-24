package com.example.vitalco.repository

import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.UsuariosDao
import com.example.vitalco.data.repository.UsuariosRepositoryImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class UsuariosRepositoryTest : StringSpec({

    val usuariosDao = mockk<UsuariosDao>()
    val apiService = mockk<ApiService>()
    val repository = UsuariosRepositoryImpl(usuariosDao, apiService)

    "login should succeed with valid credentials" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        coEvery { usuariosDao.getUserByEmail("agua@example.com") } returns usuario

        val result = repository.login("agua@example.com", "password123")

        result.isSuccess shouldBe true
        result.getOrNull()?.email shouldBe "agua@example.com"
    }

    "login should fail with invalid password" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        coEvery { usuariosDao.getUserByEmail("agua@example.com") } returns usuario

        val result = repository.login("agua@example.com", "wrongpassword")

        result.isFailure shouldBe true
    }

    "register should create usuario when email not exists" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        coEvery { usuariosDao.getUserByEmail("agua@example.com") } returns null
        coEvery { apiService.createUsuarios(any()) } returns Response.success(usuario)
        coEvery { usuariosDao.insertUser(usuario) } returns Unit

        val result = repository.register("Agua", "agua@example.com", "password123")

        result.isSuccess shouldBe true
    }

    "register should fail when email already exists" {
        val existingUsuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        coEvery { usuariosDao.getUserByEmail("agua@example.com") } returns existingUsuario

        val result = repository.register("Agua", "agua@example.com", "password123")

        result.isFailure shouldBe true
    }

    "getUserByEmail should return usuario" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        coEvery { usuariosDao.getUserByEmail("agua@example.com") } returns usuario

        val result = repository.getUserByEmail("agua@example.com")

        result?.email shouldBe "agua@example.com"
    }
})
