package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos_stock")
data class MovimientosStock(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val idProducto: Int,
        val tipoMovimiento: String,
        val cantidad: Int,
        val fecha: String
)

