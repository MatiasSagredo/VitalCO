package com.example.vitalco.data.validation

object AddProductValidation {
    
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio"
            name.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            name.length > 100 -> "El nombre no puede exceder 100 caracteres"
            else -> null
        }
    }

    fun validateSku(sku: String): String? {
        return when {
            sku.isBlank() -> "El SKU es obligatorio"
            sku.length < 2 -> "El SKU debe tener al menos 2 caracteres"
            sku.length > 50 -> "El SKU no puede exceder 50 caracteres"
            !sku.matches(Regex("^[A-Za-z0-9\\-_]+$")) -> "El SKU solo puede contener letras, números, guiones y guiones bajos"
            else -> null
        }
    }

    fun validateDescription(description: String): String? {
        return when {
            description.length > 500 -> "La descripción no puede exceder 500 caracteres"
            else -> null
        }
    }

    fun validateStock(stock: String): String? {
        return when {
            stock.isBlank() -> "El stock es obligatorio"
            stock.toIntOrNull() == null -> "El stock debe ser un número entero"
            stock.toInt() < 0 -> "El stock no puede ser negativo"
            stock.toInt() > 1000000 -> "El stock no puede exceder 1,000,000"
            else -> null
        }
    }

    fun validateMinStock(minStock: String): String? {
        return when {
            minStock.isBlank() -> "El stock mínimo es obligatorio"
            minStock.toIntOrNull() == null -> "El stock mínimo debe ser un número entero"
            minStock.toInt() < 0 -> "El stock mínimo no puede ser negativo"
            minStock.toInt() > 1000000 -> "El stock mínimo no puede exceder 1,000,000"
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

    fun validateAll(name: String, sku: String, description: String, stock: String, minStock: String, price: String): String? {
        return validateName(name)
            ?: validateSku(sku)
            ?: validateDescription(description)
            ?: validateStock(stock)
            ?: validateMinStock(minStock)
            ?: validatePrice(price)
    }
}
