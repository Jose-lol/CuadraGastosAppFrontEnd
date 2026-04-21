package com.joseantonio.norte.lopez.amediasApp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Usuario
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioRepository(requireContext: Context) {
    private val api = RetrofitClient.instanceUsuario

    // Esta es una "suspend function", solo se puede llamar desde una corrutina
    suspend fun registrar(email: String, pass: String): Response<UsuarioResponse> {
        // Retrofit se encarga de cambiar de hilo por ti automáticamente
        return api.registrarUsuario(Usuario(email = email, contrasena = pass))
    }

    suspend fun login(email: String, pass: String): Response<UsuarioResponse> {
        return api.login(Usuario(email = email, contrasena = pass))
    }
}
