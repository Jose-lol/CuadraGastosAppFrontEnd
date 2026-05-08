package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GoogleLoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.LoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.RegistroRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.TokenRefreshRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.response.JwtResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/registrar")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<UsuarioResponse?>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest : LoginRequest): Response<JwtResponse?>

    @POST("/auth/logout")
    suspend fun logout(@Header("Authorization") token: String?): Response<Map<String, String>>

    @POST("/auth/refrescar")
    suspend fun refrescarToken(@Body request: TokenRefreshRequest): Response<JwtResponse?>

    @POST("/auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest?): Response<JwtResponse?>

}