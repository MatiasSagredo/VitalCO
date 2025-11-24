package com.example.vitalco.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.vitalco.data.model.Usuarios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(context: Context) {
    private val prefs: SharedPreferences by lazy {
        try {
            context.getSharedPreferences("vitalco_session", Context.MODE_PRIVATE)
        } catch (e: Exception) {
            e.printStackTrace()
            context.getSharedPreferences("vitalco_session", Context.MODE_PRIVATE)
        }
    }

    private val _currentUser = MutableStateFlow<Usuarios?>(null)
    val currentUser: StateFlow<Usuarios?> = _currentUser.asStateFlow()

    init {
        try {
            _currentUser.value = loadUser()
        } catch (e: Exception) {
            e.printStackTrace()
            _currentUser.value = null
        }
    }

    fun saveUser(Usuarios: Usuarios) {
        try {
            prefs.edit().apply {
                Usuarios.id?.let { putInt("user_id", it) }
                putString("user_nombre", Usuarios.nombre)
                putString("user_email", Usuarios.email)
                putString("user_password", Usuarios.password)
                putString("user_rol", Usuarios.rol)
                putString("user_createdAt", Usuarios.creadoEn)
                apply()
            }
            _currentUser.value = Usuarios
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadUser(): Usuarios? {
        return try {
            val id = prefs.getInt("user_id", -1)
            if (id != -1) {
                Usuarios(
                    id = id,
                    nombre = prefs.getString("user_nombre", "") ?: "",
                    email = prefs.getString("user_email", "") ?: "",
                    password = prefs.getString("user_password", "") ?: "",
                    rol = prefs.getString("user_rol", "") ?: "",
                    creadoEn = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault()).format(java.util.Date())
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun logout() {
        try {
            prefs.edit().clear().apply()
            _currentUser.value = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isLoggedIn(): Boolean {
        return try {
            prefs.getInt("user_id", -1) != -1
        } catch (e: Exception) {
            false
        }
    }
}

