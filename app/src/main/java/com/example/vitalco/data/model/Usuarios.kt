package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuarios(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val nombre: String,
        val email: String,
        val password: String = "",
        val rol: String,
        val creadoEn: Long = 0L
)

