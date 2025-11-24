package com.example.vitalco.model

import com.example.vitalco.data.model.MovimientosStock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MovimientosStockTest : StringSpec({

    "crear movimiento entrada" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.id shouldBe 1
        movimiento.idProducto shouldBe 1
        movimiento.tipoMovimiento shouldBe "entrada"
        movimiento.cantidad shouldBe 1500
    }

    "crear movimiento salida" {
        val movimiento = MovimientosStock(
            id = 2,
            idProducto = 1,
            tipoMovimiento = "salida",
            cantidad = 500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.tipoMovimiento shouldBe "salida"
        movimiento.cantidad shouldBe 500
    }

    "movimiento tiene producto asociado" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.idProducto shouldBe 1
    }

    "movimiento cantidad positiva" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        (movimiento.cantidad > 0) shouldBe true
    }

    "movimiento tiene fecha" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.fecha.isNotEmpty() shouldBe true
    }

    "movimiento es entrada" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.tipoMovimiento.lowercase() shouldBe "entrada"
    }

    "movimiento es salida" {
        val movimiento = MovimientosStock(
            id = 2,
            idProducto = 1,
            tipoMovimiento = "salida",
            cantidad = 500,
            fecha = "2025-11-24 10:15:45"
        )

        movimiento.tipoMovimiento.lowercase() shouldBe "salida"
    }

    "movimiento valores validos" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        (movimiento.id != null) shouldBe true
        (movimiento.idProducto != null) shouldBe true
        (movimiento.cantidad > 0) shouldBe true
        movimiento.tipoMovimiento.isNotEmpty() shouldBe true
    }
})
