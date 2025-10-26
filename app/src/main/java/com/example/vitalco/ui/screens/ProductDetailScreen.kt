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
import com.example.vitalco.data.remote.model.Product
import com.example.vitalco.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: HomeViewModel = viewModel(),
    onBack: () -> Unit
) {
    var currentProduct by remember { mutableStateOf(product) }
    var isEditMode by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAdjustStockDialog by remember { mutableStateOf(false) }
    var showSellDialog by remember { mutableStateOf(false) }
    var showBuyDialog by remember { mutableStateOf(false) }

    // Campos editables
    var editName by remember { mutableStateOf(product.name) }
    var editDescription by remember { mutableStateOf(product.description) }
    var editPrice by remember { mutableStateOf(product.priceClp.toString()) }

    // Observar cambios en los productos del viewmodel
    val products by viewModel.products.collectAsState()
    LaunchedEffect(products) {
        val updated = products.find { it.id == product.id }
        if (updated != null) {
            currentProduct = updated
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Producto" else currentProduct.name) },
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
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editDescription,
                    onValueChange = { editDescription = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editPrice,
                    onValueChange = { editPrice = it },
                    label = { Text("Precio (CLP)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val updatedProduct = product.copy(
                            name = editName,
                            description = editDescription,
                            priceClp = editPrice.toDoubleOrNull() ?: product.priceClp
                        )
                        viewModel.updateProductStock(updatedProduct, updatedProduct.currentStock)
                        isEditMode = false
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
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que quieres eliminar ${currentProduct.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProduct(currentProduct)
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
                viewModel.updateProductStock(currentProduct, newStock)
                showAdjustStockDialog = false
            }
        )
    }

    if (showSellDialog) {
        SellQuantityDialog(
            product = currentProduct,
            onDismiss = { showSellDialog = false },
            onSell = { quantity ->
                val newStock = (currentProduct.currentStock - quantity).coerceAtLeast(0)
                viewModel.updateProductStock(currentProduct, newStock)
                showSellDialog = false
            }
        )
    }

    if (showBuyDialog) {
        BuyQuantityDialog(
            product = currentProduct,
            onDismiss = { showBuyDialog = false },
            onBuy = { quantity ->
                val newStock = currentProduct.currentStock + quantity
                viewModel.updateProductStock(currentProduct, newStock)
                showBuyDialog = false
            }
        )
    }
}

@Composable
fun ProductDetailCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DetailRow("Nombre", product.name)
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Descripción", product.description.ifEmpty { "Sin descripción" })
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("SKU", product.sku)
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Stock Actual", product.currentStock.toString())
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Stock Mínimo", product.minStock.toString())
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Precio", "$${product.priceClp.toBigDecimal()}CLP")
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
    product: Product,
    onDismiss: () -> Unit,
    onAdjust: (Int) -> Unit
) {
    var newStock by remember { mutableStateOf(product.currentStock.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajustar Stock") },
        text = {
            Column {
                Text("Stock actual: ${product.currentStock}")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newStock,
                    onValueChange = { newStock = it },
                    label = { Text("Nuevo Stock") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val quantity = newStock.toIntOrNull() ?: product.currentStock
                    onAdjust(quantity)
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
    product: Product,
    onDismiss: () -> Unit,
    onSell: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Vender ${product.name}") },
        text = {
            Column {
                Text("Stock disponible: ${product.currentStock}")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Cantidad a Vender") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    if (qty > 0 && qty <= product.currentStock) {
                        onSell(qty)
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
    product: Product,
    onDismiss: () -> Unit,
    onBuy: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Comprar ${product.name}") },
        text = {
            Column {
                Text("Stock actual: ${product.currentStock}")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Cantidad a Comprar") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    if (qty > 0) {
                        onBuy(qty)
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
