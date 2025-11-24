package com.example.vitalco.repository

import com.example.vitalco.data.model.MovimientosStock
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.MovimientosStockDao
import com.example.vitalco.data.repository.MovimientosStockRepositoryImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class MovimientosStockRepositoryTest : StringSpec({

    val movimientosDao = mockk<MovimientosStockDao>()
    val apiService = mockk<ApiService>()
    val repository = MovimientosStockRepositoryImpl(movimientosDao, apiService)

    "addMovimientoStock should save movimiento successfully" {
        val movimiento = MovimientosStock(
            id = null,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        coEvery { apiService.createMovimientosStock(movimiento) } returns Response.success(movimiento.copy(id = 1))
        coEvery { movimientosDao.insertMovimiento(movimiento) } returns 1L

        repository.addMovimientoStock(movimiento)

        movimiento.idProducto shouldBe 1
    }

    "getMovimientosStock should return all movimientos" {
        val movimientos = listOf(
            MovimientosStock(1, 1, "entrada", 1500, "2025-11-24 10:15:45"),
            MovimientosStock(2, 1, "salida", 500, "2025-11-24 14:45:00")
        )

        coEvery { movimientosDao.getAllMovimientos() } returns movimientos
        coEvery { apiService.getMovimientosStock() } returns Response.success(movimientos)
        coEvery { movimientosDao.deleteAllMovimientos() } returns Unit
        coEvery { movimientosDao.insertMovimientos(movimientos) } returns Unit

        val result = repository.getMovimientosStock()

        result.size shouldBe 2
    }

    "getMovimientoById should return movimiento by id" {
        val id = 1
        val movimiento = MovimientosStock(id, 1, "entrada", 1500, "2025-11-24 10:15:45")

        coEvery { apiService.getMovimientosStockById(id) } returns Response.success(movimiento)
        coEvery { movimientosDao.insertMovimiento(movimiento) } returns 1L
        coEvery { movimientosDao.getMovimientoById(id) } returns movimiento

        val result = repository.getMovimientoStockById(id)

        result?.idProducto shouldBe 1
        result?.tipoMovimiento shouldBe "entrada"
    }

    "getMovimientosStock should return local data when API fails" {
        val movimientos = listOf(
            MovimientosStock(1, 1, "entrada", 1500, "2025-11-24 10:15:45")
        )

        coEvery { movimientosDao.getAllMovimientos() } returns movimientos
        coEvery { apiService.getMovimientosStock() } throws Exception("API Error")

        val result = repository.getMovimientosStock()

        result.size shouldBe 1
    }

    "deleteMovimientoStock should delete movimiento" {
        val movimiento = MovimientosStock(
            id = 1,
            idProducto = 1,
            tipoMovimiento = "entrada",
            cantidad = 1500,
            fecha = "2025-11-24 10:15:45"
        )

        coEvery { apiService.deleteMovimientosStock(1) } returns Response.success(Unit)
        coEvery { movimientosDao.deleteMovimiento(movimiento) } returns Unit

        repository.deleteMovimientoStock(movimiento)

        movimiento.id shouldBe 1
    }
})
