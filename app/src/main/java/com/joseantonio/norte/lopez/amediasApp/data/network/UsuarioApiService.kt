package com.joseantonio.norte.lopez.amediasApp.data.network

import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UsuarioApiService {
    @POST("/usuarios/guardar")
    suspend fun registrarUsuario(@Body datos: Usuario): Response<UsuarioResponse>

    @POST("/usuarios/login")
    suspend fun login(@Body login: Usuario): Response<UsuarioResponse>
}