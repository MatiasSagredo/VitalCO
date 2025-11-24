package com.example.vitalco.model

import com.example.vitalco.data.model.Productos
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProductosTest : StringSpec({

    "crear producto with all fields" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 20,
            unidad = "unidad"
        )

        producto.id shouldBe 1
        producto.nombre shouldBe "Agua"
        producto.precio shouldBe 1500.0
        producto.stock_actual shouldBe 20
    }

    "producto en bajo stock" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 10,
            stock_actual = 5,
            unidad = "unidad"
        )

        (producto.stock_actual < producto.stock_minimo) shouldBe true
    }

    "producto no en bajo stock" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 15,
            unidad = "unidad"
        )

        (producto.stock_actual < producto.stock_minimo) shouldBe false
    }

    "calcular valor total del inventario" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 2,
            stock_actual = 10,
            unidad = "unidad"
        )

        val totalValue = producto.precio * producto.stock_actual
        totalValue shouldBe 15000.0
    }

    "producto nombre no vacio" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "unidad"
        )

        producto.nombre.isNotEmpty() shouldBe true
    }

    "producto precio positivo" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "unidad"
        )

        (producto.precio > 0) shouldBe true
    }

    "producto stock actual valido" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "unidad"
        )

        (producto.stock_actual >= 0) shouldBe true
    }

    "producto stock minimo valido" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "unidad"
        )

        (producto.stock_minimo >= 0) shouldBe true
    }
})
