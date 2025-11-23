package com.example.vitalco.data.validation

object BuyValidation {
    
    fun validateQuantity(quantity: String): String? {
        return when {
            quantity.isBlank() -> "La cantidad es obligatoria"
            quantity.toIntOrNull() == null -> "La cantidad debe ser un número entero"
            quantity.toInt() <= 0 -> "La cantidad debe ser mayor a 0"
            quantity.toInt() > 1000000 -> "La cantidad no puede exceder 1,000,000"
            else -> null
        }
    }
}

