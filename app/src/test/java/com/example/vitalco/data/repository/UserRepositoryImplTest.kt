package com.example.vitalco.data.repository

import com.example.vitalco.data.remote.dao.UserDao
import com.example.vitalco.data.remote.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var userDao: FakeUserDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        userDao = FakeUserDao()
        repository = UserRepositoryImpl(userDao)
    }

    @Test
    fun login_returnsSuccessWhenCredentialsMatch() = runTest {
        val storedUser = User(username = "john", email = "john@example.com", password = "secret")
        userDao.addUser(storedUser)

        val result = repository.login("john", "secret")

        assertTrue(result.isSuccess)
        assertEquals("john", result.getOrThrow().username)
    }

    @Test
    fun login_returnsFailureWhenFieldsAreBlank() = runTest {
        val result = repository.login("", "")

        assertTrue(result.isFailure)
        assertEquals("Por favor completa todos los campos", result.exceptionOrNull()?.message)
    }

    @Test
    fun login_returnsFailureWhenUserMissing() = runTest {
        val result = repository.login("ghost", "secret")

        assertTrue(result.isFailure)
        assertEquals("El usuario 'ghost' no existe", result.exceptionOrNull()?.message)
    }

    @Test
    fun login_returnsFailureWhenPasswordInvalid() = runTest {
        val storedUser = User(username = "john", email = "john@example.com", password = "secret")
        userDao.addUser(storedUser)

        val result = repository.login("john", "wrong")

        assertTrue(result.isFailure)
        assertEquals("Contraseña incorrecta para 'john'", result.exceptionOrNull()?.message)
    }

    @Test
    fun register_createsNewUser() = runTest {
        val result = repository.register("john", "john@example.com", "secrets")

        assertTrue(result.isSuccess)
        val stored = userDao.getUserByUsername("john")
        assertNotNull(stored)
        assertEquals("john@example.com", stored?.email)
    }

    @Test
    fun register_returnsFailureWhenUsernameExists() = runTest {
        userDao.addUser(User(username = "john", email = "john@example.com", password = "secret"))

        val result = repository.register("john", "other@example.com", "secrets")

        assertTrue(result.isFailure)
        assertEquals("El usuario 'john' ya está registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun register_returnsFailureWhenEmailExists() = runTest {
        userDao.addUser(User(username = "john", email = "john@example.com", password = "secret"))

        val result = repository.register("mike", "john@example.com", "secrets")

        assertTrue(result.isFailure)
        assertEquals("El email 'john@example.com' ya está en uso", result.exceptionOrNull()?.message)
    }

    @Test
    fun register_returnsFailureWhenFieldsBlank() = runTest {
        val result = repository.register("", "", "")

        assertTrue(result.isFailure)
        assertEquals("Todos los campos son obligatorios", result.exceptionOrNull()?.message)
    }

    @Test
    fun register_returnsFailureWhenPasswordTooShort() = runTest {
        val result = repository.register("john", "john@example.com", "123")

        assertTrue(result.isFailure)
        assertEquals("La contraseña debe tener mínimo 6 caracteres", result.exceptionOrNull()?.message)
    }

    @Test
    fun getUserByUsername_returnsStoredUser() = runTest {
        userDao.addUser(User(username = "john", email = "john@example.com", password = "secret"))

        val user = repository.getUserByUsername("john")

        assertNotNull(user)
        assertEquals("john@example.com", user?.email)
    }

    @Test
    fun getUserByEmail_returnsStoredUser() = runTest {
        userDao.addUser(User(username = "john", email = "john@example.com", password = "secret"))

        val user = repository.getUserByEmail("john@example.com")

        assertNotNull(user)
        assertEquals("john", user?.username)
    }

    @Test
    fun getUserByEmail_returnsNullWhenMissing() = runTest {
        val user = repository.getUserByEmail("missing@example.com")

        assertNull(user)
    }

    private class FakeUserDao : UserDao {
        private val mutex = Mutex()
        private val users = mutableListOf<User>()
        private var nextId = 1
        private val usersFlow = MutableStateFlow<List<User>>(emptyList())

        override suspend fun addUser(user: User) {
            mutex.withLock {
                val withId = if (user.id == 0) user.copy(id = nextId++) else user
                users.removeAll { it.id == withId.id }
                users.add(withId)
                publish()
            }
        }

        override suspend fun getUserByUsername(username: String): User? = mutex.withLock {
            users.firstOrNull { it.username == username }
        }

        override suspend fun getUserByEmail(email: String): User? = mutex.withLock {
            users.firstOrNull { it.email == email }
        }

        override fun getAllUsers(): Flow<List<User>> = usersFlow

        override suspend fun deleteUser(user: User) {
            mutex.withLock {
                users.removeAll { it.id == user.id }
                publish()
            }
        }

        private fun publish() {
            usersFlow.value = users.sortedByDescending { it.id }
        }
    }
}
