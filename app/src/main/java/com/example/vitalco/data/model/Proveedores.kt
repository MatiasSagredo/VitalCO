package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class Proveedores(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val nombre: String,
        val telefono: String,
        val direccion: String
)

