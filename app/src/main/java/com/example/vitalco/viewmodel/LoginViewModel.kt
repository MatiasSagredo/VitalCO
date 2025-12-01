package com.example.vitalco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vitalco.data.remote.AppDatabase
import com.example.vitalco.data.model.Usuarios
import com.example.vitalco.data.remote.RetrofitInstance
import com.example.vitalco.data.repository.UsuariosRepositoryImpl
import com.example.vitalco.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val usuariosRepository = UsuariosRepositoryImpl(
        AppDatabase.getDatabase(application).userDao(),
        RetrofitInstance.apiService
    )
    private val sessionManager = SessionManager(application)

    suspend fun login(nombreUsuario: String, contrasena: String): Result<Usuarios> {
        return withContext(Dispatchers.IO) {
            usuariosRepository.login(nombreUsuario, contrasena).also { result ->
                result.getOrNull()?.let { user ->
                    sessionManager.saveUser(user)
                }
            }
        }
    }

    suspend fun register(nombreUsuario: String, correo: String, contrasena: String): Result<Usuarios> {
        return withContext(Dispatchers.IO) {
            usuariosRepository.register(nombreUsuario, correo, contrasena).also { result ->
                result.getOrNull()?.let { user ->
                    sessionManager.saveUser(user)
                }
            }
        }
    }
}
