package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ventas")
data class Ventas(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val idCliente: Int,
        val idUsuario: Int,
        val fecha: Long,
        val total: Double
)

