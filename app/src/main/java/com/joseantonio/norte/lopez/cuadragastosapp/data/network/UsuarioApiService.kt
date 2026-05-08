package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface UsuarioApiService {
    @POST("/usuarios/guardar")
    suspend fun registrarUsuario(@Body datos: Usuario): Response<UsuarioResponse>

    @POST("/usuarios/login")
    suspend fun login(@Body login: Usuario): Response<UsuarioResponse>

    @GET("/usuarios/cargar")
    suspend fun cargarAmigos(@Query("idUsuario") idUsuario: Int?): Response<Collection<UsuarioResponse>>
}