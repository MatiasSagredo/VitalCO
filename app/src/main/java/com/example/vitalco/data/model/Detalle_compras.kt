package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detalle_compras")
data class DetalleCompras(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val idCompra: Int,
        val idProducto: Int,
        val cantidad: Int,
        val costoUnitario: Double,
        val subtotal: Double
)

