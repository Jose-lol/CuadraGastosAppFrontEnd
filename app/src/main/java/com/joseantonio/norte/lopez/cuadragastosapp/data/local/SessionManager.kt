package com.joseantonio.norte.lopez.cuadragastosapp.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

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

    fun saveSession(access: String?, refresh: String?,idUsuario: Int?) {
        prefs.edit().apply {
            putString("access_token", access)
            putString("refresh_token", refresh)
            putInt("idUsuario", idUsuario?: -1)
            apply()
        }
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}