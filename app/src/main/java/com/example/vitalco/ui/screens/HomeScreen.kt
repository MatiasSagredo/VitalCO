package com.example.vitalco.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column as ComposeColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.vitalco.navigation.Routes
import com.example.vitalco.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController? = null, onNavigateToMovimientos: () -> Unit = {}) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: HomeViewModel = viewModel()
    val ultimoMovimiento by viewModel.ultimoMovimiento.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUltimoMovimiento()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ComposeColumn(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp, 48.dp)
            ) {
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    selected = false,
                    onClick = {
                        navController?.navigate(Routes.PRODUCTS)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Productos") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Productos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Acerca de") },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Acerca de") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("VitalCO") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                UltimoMovimientoWidget(
                    movimiento = ultimoMovimiento,
                    onClick = onNavigateToMovimientos,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun UltimoMovimientoWidget(
    movimiento: com.example.vitalco.data.model.MovimientosStock?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (movimiento?.tipoMovimiento) {
        "entrada" -> MaterialTheme.colorScheme.tertiaryContainer
        "salida" -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val textColor = when (movimiento?.tipoMovimiento) {
        "entrada" -> MaterialTheme.colorScheme.onTertiaryContainer
        "salida" -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = "Último Movimiento",
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        if (movimiento != null) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movimiento.tipoMovimiento,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Producto ID: ${movimiento.idProducto}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor
                    )
                }
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.End
                ) {
                    Text(
                        text = "Cantidad: ${movimiento.cantidad}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = movimiento.fecha,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca para ver todos los movimientos",
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Text(
                text = "No hay movimientos registrados",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Los movimientos aparecerán aquí cuando hagas compras o ventas",
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

