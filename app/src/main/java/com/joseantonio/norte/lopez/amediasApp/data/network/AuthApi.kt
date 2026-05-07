package com.joseantonio.norte.lopez.amediasApp.data.network

import com.joseantonio.norte.lopez.amediasApp.data.dto.request.LoginRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.RegistroRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.TokenRefreshRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.JwtResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse

import retrofit2.Call
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

}