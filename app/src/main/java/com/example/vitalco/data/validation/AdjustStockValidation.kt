package com.example.vitalco.data.validation

object AdjustStockValidation {
    
    fun validateNewStock(newStock: String): String? {
        return when {
            newStock.isBlank() -> "El stock es obligatorio"
            newStock.toIntOrNull() == null -> "El stock debe ser un número entero"
            newStock.toInt() < 0 -> "El stock no puede ser negativo"
            newStock.toInt() > 1000000 -> "El stock no puede exceder 1,000,000"
            else -> null
        }
    }
}

