package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Clientes(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val nombre: String,
        val direccion: String,
        val telefono: String
)

