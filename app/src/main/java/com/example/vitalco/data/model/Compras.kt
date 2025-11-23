package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class Compras(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val idProveedor: Int,
        val idUsuario: Int,
        val fecha: String,
        val total: Double
)

