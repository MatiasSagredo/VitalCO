package com.example.vitalco.data.repository

import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.remote.ApiService
import com.example.vitalco.data.remote.dao.UsuariosDao
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class UsuariosRepositoryImpl(
    private val usuariosDao: UsuariosDao,
    private val apiService: ApiService
) : UsuariosRepository {

    override suspend fun uploadImage(file: java.io.File): String? {
        return suspendCancellableCoroutine { continuation ->
            try {
                MediaManager.get().upload(file.absolutePath)
                    .unsigned("img_profile") // Reemplazar con tu upload preset real
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String?) {}
                        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                        override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                            val url = resultData?.get("secure_url") as? String
                            if (continuation.isActive) {
                                continuation.resume(url)
                            }
                        }

                        override fun onError(requestId: String?, error: ErrorInfo?) {
                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        }

                        override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                    })
                    .dispatch()
            } catch (e: Exception) {
                e.printStackTrace()
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun login(nombreUsuario: String, contrasena: String): Result<Usuarios> {
        return try {
            if (nombreUsuario.isBlank() || contrasena.isBlank()) {
                throw Exception("Por favor completa todos los campos")
            }

            val usuarioLocal = usuariosDao.getUserByUsername(nombreUsuario)

            if (usuarioLocal != null) {
                if (usuarioLocal.password == contrasena) {
                    return Result.success(usuarioLocal)
                } else {
                    throw Exception("Contraseña incorrecta para '$nombreUsuario'")
                }
            } else {
                try {
                    val respuesta = apiService.getUsuarioByUsername(nombreUsuario)
                    if (respuesta.isSuccessful) {
                        val usuarioRemoto = respuesta.body()
                        if (usuarioRemoto != null) {
                            if (usuarioRemoto.password == contrasena) {
                                usuariosDao.insertUser(usuarioRemoto)
                                return Result.success(usuarioRemoto)
                            } else {
                                throw Exception("Contraseña incorrecta para '$nombreUsuario'")
                            }
                        } else {
                            throw Exception("El usuario '$nombreUsuario' no existe")
                        }
                    } else {
                        throw Exception("El usuario '$nombreUsuario' no existe")
                    }
                } catch (e: Exception) {
                    if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
                         throw Exception("No se pudo conectar al servidor. Inténtalo más tarde.")
                    }
                    throw e
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        nombreUsuario: String,
        correo: String,
        contrasena: String
    ): Result<Usuarios> {
        return try {
            if (nombreUsuario.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                throw Exception("Todos los campos son obligatorios")
            }

            if (contrasena.length < 6) {
                throw Exception("La contraseña debe tener mínimo 6 caracteres")
            }

            val correoExistente = usuariosDao.getUserByEmail(correo)
            if (correoExistente != null) {
                throw Exception("El email '$correo' ya está en uso")
            }

            val fecha = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            val nuevoUsuario = Usuarios(id = null, nombre = nombreUsuario, email = correo, password = contrasena, rol = "user", creadoEn = fecha)

            usuariosDao.insertUser(nuevoUsuario)

            try {
                apiService.createUsuarios(nuevoUsuario)
            } catch (e: Exception) {
                println("Error al sincronizar el registro con el servidor: ${e.message}")
            }

            Result.success(nuevoUsuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Usuarios? {
        return usuariosDao.getUserByEmail(email)
    }

    override suspend fun refreshUserById(id: Int): Usuarios? {
        return try {
            val response = apiService.getUsuarioById(id)
            if (response.isSuccessful) {
                val usuarioRemoto = response.body()
                if (usuarioRemoto != null) {
                    usuariosDao.insertUser(usuarioRemoto)
                    return usuarioRemoto
                }
            }
            usuariosDao.getUserById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            usuariosDao.getUserById(id)
        }
    }

    override suspend fun updateUser(user: Usuarios): Result<Usuarios> {
        return try {
            // 1. Si la imagen es local (no empieza con http), intentamos subirla
            var imageUrl = user.imagen
            if (user.imagen != null && !user.imagen.startsWith("http")) {
                val file = java.io.File(user.imagen)
                if (file.exists()) {
                    val urlSubida = uploadImage(file)
                    if (urlSubida != null) {
                        imageUrl = urlSubida
                    }
                }
            }

            val usuarioActualizado = user.copy(imagen = imageUrl)

            // Offline first: actualizar base de datos local
            usuariosDao.updateUser(usuarioActualizado)

            // Intentar actualizar en API
            try {
                if (usuarioActualizado.id != null) {
                    val updateRequest = com.example.vitalco.data.model.UserUpdateRequest(
                        nombre = usuarioActualizado.nombre,
                        email = usuarioActualizado.email,
                        password = usuarioActualizado.password,
                        rol = usuarioActualizado.rol,
                        imagen = usuarioActualizado.imagen
                    )
                    apiService.updateUsuarios(usuarioActualizado.id, updateRequest)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Result.success(usuarioActualizado)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
