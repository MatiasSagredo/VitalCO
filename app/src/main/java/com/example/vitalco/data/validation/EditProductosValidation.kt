package com.example.vitalco.data.validation

object EditProductValidation {
    
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio"
            name.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            name.length > 100 -> "El nombre no puede exceder 100 caracteres"
            else -> null
        }
    }

    fun validateDescription(description: String): String? {
        return when {
            description.length > 500 -> "La descripción no puede exceder 500 caracteres"
            else -> null
        }
    }

    fun validatePrice(price: String): String? {
        return when {
            price.isBlank() -> "El precio es obligatorio"
            price.toDoubleOrNull() == null -> "El precio debe ser un número válido"
            price.toDouble() < 0 -> "El precio no puede ser negativo"
            price.toDouble() > 999999999.99 -> "El precio es demasiado alto"
            else -> null
        }
    }

    fun validateAll(name: String, description: String, price: String): String? {
        return validateName(name)
            ?: validateDescription(description)
            ?: validatePrice(price)
    }
}

