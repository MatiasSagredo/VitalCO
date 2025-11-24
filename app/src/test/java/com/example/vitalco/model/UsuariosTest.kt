package com.example.vitalco.model

import com.example.vitalco.data.model.Usuarios
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UsuariosTest : StringSpec({

    "crear usuario with all fields" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.id shouldBe 1
        usuario.nombre shouldBe "Agua"
        usuario.email shouldBe "agua@example.com"
        usuario.rol shouldBe "admin"
    }

    "usuario nombre no vacio" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.nombre.isNotEmpty() shouldBe true
    }

    "usuario email valido" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.email.contains("@") shouldBe true
    }

    "usuario password no vacio" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.password.isNotEmpty() shouldBe true
    }

    "usuario tiene rol asignado" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.rol.isNotEmpty() shouldBe true
    }

    "usuario rol admin" {
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.rol shouldBe "admin"
    }

    "usuario rol user" {
        val usuario = Usuarios(
            id = 2,
            nombre = "Agua",
            email = "agua2@example.com",
            password = "password456",
            rol = "user",
            creadoEn = "2025-11-24 10:15:45"
        )

        usuario.rol shouldBe "user"
    }

    "usuario tiene fecha creacion" {
        val fecha = "2025-11-24 10:15:45"
        val usuario = Usuarios(
            id = 1,
            nombre = "Agua",
            email = "agua@example.com",
            password = "password123",
            rol = "admin",
            creadoEn = fecha
        )

        usuario.creadoEn shouldBe fecha
    }
})
