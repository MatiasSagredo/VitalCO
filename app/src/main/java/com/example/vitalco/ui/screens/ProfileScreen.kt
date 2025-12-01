package com.example.vitalco.ui.screens

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vitalco.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel? = null,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentUser by mainViewModel?.currentUser?.collectAsState() ?: remember { mutableStateOf(null) }
    var username by remember(currentUser) { mutableStateOf(currentUser?.nombre ?: "") }
    var email by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var photoPath by remember(currentUser) { mutableStateOf(currentUser?.imagen) }

    LaunchedEffect(Unit) {
        mainViewModel?.refreshCurrentUser()
    }

    LaunchedEffect(photoPath) {
        if (photoPath != null) {
            println("DEBUG: Iniciando carga de imagen. Path: $photoPath")
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    if (photoPath!!.startsWith("http")) {
                        println("DEBUG: Cargando desde URL")
                        // Cargar desde URL
                        val url = java.net.URL(photoPath)
                        android.graphics.BitmapFactory.decodeStream(url.openStream())
                    } else {
                        println("DEBUG: Cargando desde archivo local")
                        // Cargar desde archivo local
                        val file = File(photoPath!!)
                        if (file.exists()) {
                            println("DEBUG: Archivo local existe: ${file.absolutePath}")
                            android.graphics.BitmapFactory.decodeFile(file.absolutePath)
                        } else {
                            println("DEBUG: Archivo local NO existe: ${file.absolutePath}")
                            null
                        }
                    }
                } catch (e: Exception) {
                    println("DEBUG: Excepción al cargar imagen: ${e.message}")
                    e.printStackTrace()
                    null
                }
            }
            if (bitmap != null) {
                println("DEBUG: Bitmap decodificado correctamente")
                photoBitmap = bitmap
            } else {
                println("DEBUG: Bitmap es null después de intentar cargar")
            }
        } else {
             println("DEBUG: photoPath es null")
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            photoBitmap = bitmap
            photoPath = saveBitmapToFile(context, bitmap, currentUser?.id.toString())
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar con ícono de cámara o foto
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                if (photoBitmap != null) {
                    Image(
                        bitmap = photoBitmap!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "Tomar foto",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (photoPath != null) "Foto guardada" else "Toca para tomar foto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (currentUser != null) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuarios") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = isEditing
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = isEditing
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isEditing) {
                    Button(
                        onClick = {
                            currentUser?.let { user ->
                                val updatedUser = user.copy(
                                    nombre = username,
                                    email = email,
                                    imagen = photoPath ?: user.imagen
                                )
                                mainViewModel?.updateUserProfile(updatedUser)
                            }
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Guardar Cambios")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            username = currentUser?.nombre ?: ""
                            email = currentUser?.email ?: ""
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Cancelar")
                    }
                } else {
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Editar Perfil")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        mainViewModel?.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Cerrar Sesión")
                }
            } else {
                Text("No hay Usuarios logueado")
            }
        }
    }
}

private fun saveBitmapToFile(context: android.content.Context, bitmap: Bitmap, userId: String): String {
    val filesDir = context.filesDir
    val file = File(filesDir, "profile_${userId}_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file.absolutePath
}
