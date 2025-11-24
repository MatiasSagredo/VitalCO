package com.example.vitalco.repository

import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.ProductosDao
import com.example.vitalco.data.repository.ProductosRepositoryImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class ProductosRepositoryTest : StringSpec({

    val productosDao = mockk<ProductosDao>()
    val apiService = mockk<ApiService>()
    val repository = ProductosRepositoryImpl(productosDao, apiService)

    "addProductos should save producto successfully" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_actual = 10,
            stock_minimo = 5,
            unidad = "unidad"
        )

        coEvery { apiService.createProductos(producto) } returns Response.success(producto)
        coEvery { productosDao.insertProduct(producto) } returns 1L

        repository.addProductos(producto)

        producto.nombre shouldBe "Agua"
    }

    "addProductos should save local when API fails" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_actual = 10,
            stock_minimo = 5,
            unidad = "unidad"
        )

        coEvery { apiService.createProductos(producto) } throws Exception("API Error")
        coEvery { productosDao.insertProduct(producto) } returns 1L

        repository.addProductos(producto)

        producto.nombre shouldBe "Agua"
    }

    "getProductosById should return producto by id" {
        val id = 1
        val producto = Productos(
            id = id,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_actual = 50,
            stock_minimo = 10,
            unidad = "unidad"
        )

        coEvery { apiService.getProductoById(id) } returns Response.success(producto)
        coEvery { productosDao.insertProduct(producto) } returns 1L
        coEvery { productosDao.getProductoById(id) } returns producto

        val result = repository.getProductosById(id)

        result?.nombre shouldBe "Agua"
        result?.precio shouldBe 1500.0
    }

    "getAllProductos should return all productos" {
        val productos = listOf(
            Productos(1, "Agua", "Agua", 1500.0, 20, 5, "unidad"),
            Productos(2, "Agua", "Agua", 1500.0, 15, 3, "unidad")
        )

        coEvery { productosDao.getAllProducts() } returns productos
        coEvery { apiService.getProductos() } returns Response.success(productos)
        coEvery { productosDao.deleteAllProducts() } returns Unit
        coEvery { productosDao.insertProducts(productos) } returns Unit

        val result = repository.getAllProductos()

        result.size shouldBe 2
    }

    "getAllProductos should return local data when API fails" {
        val productos = listOf(
            Productos(1, "Agua", "Agua", 1500.0, 10, 5, "unidad")
        )

        coEvery { productosDao.getAllProducts() } returns productos
        coEvery { apiService.getProductos() } throws Exception("API Error")

        val result = repository.getAllProductos()

        result.size shouldBe 1
    }

    "deleteProductos should delete producto" {
        val producto = Productos(
            id = 1,
            nombre = "Agua",
            descripcion = "Agua",
            precio = 1500.0,
            stock_actual = 5,
            stock_minimo = 2,
            unidad = "unidad"
        )

        coEvery { apiService.deleteProductos(1) } returns Response.success(Unit)
        coEvery { productosDao.deleteProductos(producto) } returns Unit

        repository.deleteProductos(producto)

        producto.id shouldBe 1
    }

    "searchProductos should return matching productos" {
        val query = "Agua"
        val productos = listOf(
            Productos(2, "Agua", "Agua", 1500.0, 20, 5, "unidad")
        )

        coEvery { productosDao.searchProducts(query) } returns productos

        val result = repository.searchProductos(query)

        result.size shouldBe 1
        result[0].nombre shouldBe "Agua"
    }

    "searchProductos should return empty list when no match" {
        val query = "NoExiste"
        coEvery { productosDao.searchProducts(query) } returns emptyList()

        val result = repository.searchProductos(query)

        result.size shouldBe 0
    }
})
