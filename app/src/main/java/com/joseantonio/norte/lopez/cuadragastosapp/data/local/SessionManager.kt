package com.joseantonio.norte.lopez.cuadragastosapp.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse

class SessionManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun getIdUsuario(): Int? = prefs.getInt("idUsuario", -1)

    fun getEmail(): String? = prefs.getString("email", null)

    fun getNombre(): String? = prefs.getString("nombre", null)

    fun getTelefono(): String? = prefs.getString("telefono", null)

    fun saveSession(access: String?, refresh: String?,usuario: UsuarioResponse?) {
        prefs.edit().apply {
            putString("access_token", access)
            putString("refresh_token", refresh)
            putString("email", usuario?.email)
            putString("nombre",usuario?.nombre)
            putString("telefono",usuario?.telefono)
            putInt("idUsuario", usuario?.idUsuario ?: -1)
            apply()
        }
    }

    fun updateSession(usuario: UsuarioResponse?) {
        prefs.edit().apply {
            putString("email", usuario?.email)
            putString("nombre",usuario?.nombre)
            putString("telefono",usuario?.telefono)
            putInt("idUsuario", usuario?.idUsuario ?: -1)
            apply()
        }
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}