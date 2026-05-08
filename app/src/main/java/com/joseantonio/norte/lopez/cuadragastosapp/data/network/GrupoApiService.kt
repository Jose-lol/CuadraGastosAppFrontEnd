package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface GrupoApiService {

    @GET("/grupo/cargar")
    suspend fun cargarGrupos(@Query("idUsuario") idUsuario: Int?): Response<Collection<GrupoResponse>>

    @POST("/grupo/guardar")
    suspend fun guardarGrupos(@Body grupoRequest : GrupoRequest): Response<Unit>

}