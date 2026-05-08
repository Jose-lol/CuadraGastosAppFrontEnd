package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GoogleLoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.LoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.RegistroRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.response.JwtResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class AuthRepository(requireContext: Context) {

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

    suspend fun loginWithGoogle(token: GoogleLoginRequest): Response<JwtResponse?> {
        return authApi.loginWithGoogle(token)
    }
}