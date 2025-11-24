package com.example.vitalco.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vitalco.data.model.Usuarios
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuariosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertUser(user: Usuarios): Long

    @Update suspend fun updateUser(user: Usuarios)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertUsers(users: List<Usuarios>)

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): Usuarios?

    @Query("SELECT * FROM usuarios WHERE nombre = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): Usuarios?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): Usuarios?

    @Query("SELECT * FROM usuarios ORDER BY creadoEn DESC") fun getAllUsers(): Flow<List<Usuarios>>

    @Query("SELECT COUNT(*) FROM usuarios") suspend fun getUserCount(): Int

    @Query("SELECT * FROM usuarios WHERE rol = :role ORDER BY nombre ASC")
    suspend fun getUsersByRole(role: String): List<Usuarios>

    @Query(
            "SELECT * FROM usuarios WHERE nombre LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'"
    )
    suspend fun searchUsers(query: String): List<Usuarios>

    @Delete suspend fun deleteUser(user: Usuarios)

    @Query("DELETE FROM usuarios WHERE id = :id") suspend fun deleteUserById(id: Int)

    @Query("DELETE FROM usuarios") suspend fun deleteAllUsers()
}

