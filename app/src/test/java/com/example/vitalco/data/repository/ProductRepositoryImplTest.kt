package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.dao.ProductDao
import com.example.vitalco.data.remote.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var productDao: FakeProductDao
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        productDao = FakeProductDao()
        repository = ProductRepositoryImpl(productDao)
    }

    @Test
    fun addProduct_persistsProduct() = runTest {
        val product = Product(name = "Coffee", sku = "SKU-1")

        repository.addProduct(product)

        val storedProducts = repository.getAllProducts().first()
        assertEquals(1, storedProducts.size)
        assertEquals("Coffee", storedProducts.first().name)
    }

    @Test
    fun updateProduct_replacesStoredProduct() = runTest {
        val product = Product(name = "Coffee", sku = "SKU-1", currentStock = 5)
        repository.addProduct(product)
        val stored = repository.getAllProducts().first().first()

        val updated = stored.copy(currentStock = 10)
        repository.updateProduct(updated)

        val result = repository.getProductById(updated.id)
        assertEquals(10, result?.currentStock)
    }

    @Test
    fun deleteProduct_removesProduct() = runTest {
        val product = Product(name = "Coffee", sku = "SKU-1")
        repository.addProduct(product)
        val stored = repository.getAllProducts().first().first()

        repository.deleteProduct(stored)

        val products = repository.getAllProducts().first()
        assertEquals(0, products.size)
    }

    @Test
    fun getProductBySku_returnsMatchingProduct() = runTest {
        val product = Product(name = "Coffee", sku = "SKU-1")
        repository.addProduct(product)

        val result = repository.getProductBySku("SKU-1")
        assertEquals("Coffee", result?.name)
    }

    @Test
    fun getProductById_returnsNullWhenMissing() = runTest {
        val result = repository.getProductById(999)
        assertNull(result)
    }

    @Test
    fun getLowStockProducts_emitsFilteredList() = runTest {
        val lowStock = Product(name = "Coffee", sku = "SKU-1", currentStock = 2, minStock = 5)
        val okStock = Product(name = "Tea", sku = "SKU-2", currentStock = 10, minStock = 5)
        repository.addProduct(lowStock)
        repository.addProduct(okStock)

        val result = repository.getLowStockProducts().first()
        assertEquals(listOf("Coffee"), result.map { it.name })
    }

    @Test
    fun searchProducts_returnsMatchingByName() = runTest {
        repository.addProduct(Product(name = "Colombian Coffee", sku = "SKU-1"))
        repository.addProduct(Product(name = "Green Tea", sku = "SKU-2"))

        val result = repository.searchProducts("coffee").first()
        assertEquals(1, result.size)
        assertEquals("Colombian Coffee", result.first().name)
    }

    private class FakeProductDao : ProductDao {
        private val mutex = Mutex()
        private val products = mutableListOf<Product>()
        private var nextId = 1
        private val allProductsFlow = MutableStateFlow<List<Product>>(emptyList())

        override suspend fun addProduct(product: Product) {
            mutex.withLock {
                val withId = if (product.id == 0) product.copy(id = nextId++) else product
                products.removeAll { it.id == withId.id }
                products.add(withId)
                publish()
            }
        }

        override suspend fun updateProduct(product: Product) {
            mutex.withLock {
                val index = products.indexOfFirst { it.id == product.id }
                if (index != -1) {
                    products[index] = product
                    publish()
                }
            }
        }

        override suspend fun getProductById(id: Int): Product? = mutex.withLock {
            products.firstOrNull { it.id == id }
        }

        override suspend fun getProductBySku(sku: String): Product? = mutex.withLock {
            products.firstOrNull { it.sku == sku }
        }

        override fun getAllProducts(): Flow<List<Product>> = allProductsFlow

        override fun getLowStockProducts(): Flow<List<Product>> =
            allProductsFlow.map { list ->
                list.filter { it.currentStock <= it.minStock }
                    .sortedBy { it.currentStock }
            }

        override fun searchProducts(query: String): Flow<List<Product>> =
            allProductsFlow.map { list ->
                list.filter { it.name.contains(query, ignoreCase = true) }
                    .sortedBy { it.name }
            }

        override suspend fun deleteProduct(product: Product) {
            mutex.withLock {
                products.removeAll { it.id == product.id }
                publish()
            }
        }

        private fun publish() {
            allProductsFlow.value = products.sortedBy { it.name }
        }
    }
}
