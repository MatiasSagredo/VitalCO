package com.example.vitalco.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val sku: String,
    val currentStock: Int = 0,
    val minStock: Int = 0,
    val priceClp: Double = 0.0
)

