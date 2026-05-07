package com.joseantonio.norte.lopez.amediasApp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.LoginRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.RegistroRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.JwtResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Usuario
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioRepository(requireContext: Context) {
    private val usuarioApi = RetrofitClient.instanceUsuario
    private val authApi = RetrofitClient.authApi

    suspend fun registrar(email: String, pass: String): Response<UsuarioResponse?> {
        return authApi.registrarUsuario(RegistroRequest(email = email, contrasena = pass))
    }

    suspend fun login(email: String, pass: String): Response<JwtResponse?> {
        return authApi.login(LoginRequest(email = email, contrasena = pass))
    }

    suspend fun logout(token: String?): Response<Map<String, String>> {
        return authApi.logout("Bearer $token")
    }

    suspend fun cargarAmigos(idUsuario: Int?): Response<Collection<UsuarioResponse>> {
        return usuarioApi.cargarAmigos(idUsuario)
    }
}
