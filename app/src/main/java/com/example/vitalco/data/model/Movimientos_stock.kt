package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "movimientos_stock",
    foreignKeys = [
        ForeignKey(
            entity = Productos::class,
            parentColumns = ["id"],
            childColumns = ["idProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovimientosStock(
        @PrimaryKey(autoGenerate = true)val id: Int?,
        @SerializedName("id_producto")
        val idProducto: Int?,
        @SerializedName("tipo_movimiento")
        val tipoMovimiento: String,
        val cantidad: Int,
        val fecha: String
)

