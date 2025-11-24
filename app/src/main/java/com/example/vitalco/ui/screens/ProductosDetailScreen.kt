package com.example.vitalco.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vitalco.data.model.Productos
import com.example.vitalco.data.validation.AdjustStockValidation
import com.example.vitalco.data.validation.BuyValidation
import com.example.vitalco.data.validation.EditProductValidation
import com.example.vitalco.data.validation.SellValidation
import com.example.vitalco.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Productos,
    viewModel: ProductsViewModel = viewModel(),
    onBack: () -> Unit
) {
    var currentProduct by remember { mutableStateOf(product) }
    var isEditMode by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAdjustStockDialog by remember { mutableStateOf(false) }
    var showSellDialog by remember { mutableStateOf(false) }
    var showBuyDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(product.nombre) }
    var editDescription by remember { mutableStateOf(product.descripcion) }
    var editPrice by remember { mutableStateOf(product.precio.toString()) }
    var editErrorMessage by remember { mutableStateOf<String?>(null) }

    val productos by viewModel.productos.collectAsState()
    LaunchedEffect(productos) {
        val updated = productos.find { it.id == product.id }
        if (updated != null) {
            currentProduct = updated
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Productos" else currentProduct.nombre) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditMode) isEditMode = false else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    if (!isEditMode) {
                        IconButton(onClick = { isEditMode = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (isEditMode) {
                // Modo edición
                if (editErrorMessage != null) {
                    Text(
                        text = editErrorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                OutlinedTextField(
                    value = editName,
                    onValueChange = {
                        editName = it
                        editErrorMessage = null
                    },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editDescription,
                    onValueChange = {
                        editDescription = it
                        editErrorMessage = null
                    },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editPrice,
                    onValueChange = {
                        editPrice = it
                        editErrorMessage = null
                    },
                    label = { Text("Precio (CLP)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val error = EditProductValidation.validateAll(editName, editDescription, editPrice)
                        if (error != null) {
                            editErrorMessage = error
                        } else {
                            val updatedProduct = product.copy(
                                nombre = editName,
                                descripcion = editDescription,
                                precio = editPrice.toDoubleOrNull() ?: product.precio
                            )
                            viewModel.updateProductosStock(updatedProduct, updatedProduct.stock_actual)
                            isEditMode = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }
            } else {
                // Modo lectura
                ProductDetailCard(currentProduct)
                Spacer(modifier = Modifier.height(20.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showAdjustStockDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Ajustar Stock")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showSellDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Vender")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showBuyDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Comprar")
                    }
                }
            }
        }
    }

    // Diálogos
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Productos") },
            text = { Text("¿Estás seguro de que quieres eliminar ${currentProduct.nombre}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProductos(currentProduct)
                        showDeleteDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAdjustStockDialog) {
        AdjustStockDialog(
            product = currentProduct,
            onDismiss = { showAdjustStockDialog = false },
            onAdjust = { newStock ->
                viewModel.updateProductosStock(currentProduct, newStock)
                showAdjustStockDialog = false
            }
        )
    }

    if (showSellDialog) {
        SellQuantityDialog(
            product = currentProduct,
            onDismiss = { showSellDialog = false },
            onSell = { quantity ->
                val newStock = (currentProduct.stock_actual - quantity).coerceAtLeast(0)
                viewModel.updateProductosStock(currentProduct, newStock)
                showSellDialog = false
            }
        )
    }

    if (showBuyDialog) {
        BuyQuantityDialog(
            product = currentProduct,
            onDismiss = { showBuyDialog = false },
            onBuy = { quantity ->
                val newStock = currentProduct.stock_actual + quantity
                viewModel.updateProductosStock(currentProduct, newStock)
                showBuyDialog = false
            }
        )
    }
}

@Composable
fun ProductDetailCard(product: Productos) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DetailRow("Nombre", product.nombre)
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Descripción", product.descripcion.ifEmpty { "Sin descripción" })
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Stock Actual", product.stock_actual.toString())
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Stock Mínimo", product.stock_minimo.toString())
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Precio", "$${product.precio.toBigDecimal()}CLP")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun AdjustStockDialog(
    product: Productos,
    onDismiss: () -> Unit,
    onAdjust: (Int) -> Unit
) {
    var newStock by remember { mutableStateOf(product.stock_actual.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajustar Stock") },
        text = {
            Column {
                Text("Stock actual: ${product.stock_actual}")
                Spacer(modifier = Modifier.height(12.dp))
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = newStock,
                    onValueChange = {
                        newStock = it
                        errorMessage = null
                    },
                    label = { Text("Nuevo Stock") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val error = AdjustStockValidation.validateNewStock(newStock)
                    if (error != null) {
                        errorMessage = error
                    } else {
                        val quantity = newStock.toInt()
                        onAdjust(quantity)
                        onDismiss()
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SellQuantityDialog(
    product: Productos,
    onDismiss: () -> Unit,
    onSell: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Vender ${product.nombre}") },
        text = {
            Column {
                Text("Stock disponible: ${product.stock_actual}")
                Spacer(modifier = Modifier.height(12.dp))
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        errorMessage = null
                    },
                    label = { Text("Cantidad a Vender") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val error = SellValidation.validateAll(quantity, product.stock_actual)
                    if (error != null) {
                        errorMessage = error
                    } else {
                        val qty = quantity.toInt()
                        onSell(qty)
                        onDismiss()
                    }
                }
            ) {
                Text("Vender")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun BuyQuantityDialog(
    product: Productos,
    onDismiss: () -> Unit,
    onBuy: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Comprar ${product.nombre}") },
        text = {
            Column {
                Text("Stock actual: ${product.stock_actual}")
                Spacer(modifier = Modifier.height(12.dp))
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        errorMessage = null
                    },
                    label = { Text("Cantidad a Comprar") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val error = BuyValidation.validateQuantity(quantity)
                    if (error != null) {
                        errorMessage = error
                    } else {
                        val qty = quantity.toInt()
                        onBuy(qty)
                        onDismiss()
                    }
                }
            ) {
                Text("Comprar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

