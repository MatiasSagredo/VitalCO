package com.example.vitalco.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "productos")
data class Productos(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("stock_actual")
        val stock_actual: Int,
    @SerializedName("stock_minimo")
        val stock_minimo: Int,
    val unidad: String,
    @SerializedName("creado_en")
        val creado_en: String = ""
)
