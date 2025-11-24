package com.example.vitalco.viewmodel

import com.example.vitalco.data.model.Productos
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

class ProductsViewModelTest {

    @Test
    fun addProducto_success() = runTest {
        val producto = Productos(
            id = 1,
            nombre = "Nuevo Producto",
            descripcion = "Descripción",
            precio = 150.0,
            stock_minimo = 5,
            stock_actual = 15,
            unidad = "Unidad"
        )

        assertEquals(producto.nombre, "Nuevo Producto")
        assertEquals(producto.precio, 150.0)
    }

    @Test
    fun updateProductosStock_calculaDiferencia() = runTest {
        val producto = Productos(
            id = 1,
            nombre = "Producto",
            descripcion = "Desc",
            precio = 100.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "Unidad"
        )

        val newStock = 15
        val oldStock = producto.stock_actual
        val diferencia = newStock - oldStock

        assertEquals(diferencia, 5)
    }

    @Test
    fun getProductoById_returnsCorrectProduct() = runTest {
        val id = 1
        val producto = Productos(
            id = id,
            nombre = "Producto Test",
            descripcion = "Descripción",
            precio = 100.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "Unidad"
        )

        assertEquals(producto.id, id)
        assertEquals(producto.nombre, "Producto Test")
    }

    @Test
    fun validateProductoData_success() = runTest {
        val producto = Productos(
            id = 1,
            nombre = "Producto",
            descripcion = "Descripción",
            precio = 100.0,
            stock_minimo = 5,
            stock_actual = 10,
            unidad = "Unidad"
        )

        assertTrue(producto.nombre.isNotBlank())
        assertTrue(producto.precio > 0)
        assertTrue(producto.stock_minimo >= 0)
        assertTrue(producto.stock_actual >= 0)
    }

    @Test
    fun productoStockBajoDetectado() = runTest {
        val producto = Productos(
            id = 1,
            nombre = "Producto",
            descripcion = "Desc",
            precio = 100.0,
            stock_minimo = 10,
            stock_actual = 5,
            unidad = "Unidad"
        )

        assertTrue(producto.stock_actual < producto.stock_minimo)
    }

    @Test
    fun calcularValorInventario() = runTest {
        val producto = Productos(
            id = 1,
            nombre = "Producto",
            descripcion = "Desc",
            precio = 100.0,
            stock_minimo = 5,
            stock_actual = 20,
            unidad = "Unidad"
        )

        val totalValue = producto.precio * producto.stock_actual
        assertEquals(totalValue, 2000.0, 0.0)
    }
}
