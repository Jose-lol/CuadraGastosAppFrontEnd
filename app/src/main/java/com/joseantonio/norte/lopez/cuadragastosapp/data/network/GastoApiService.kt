package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.ContactoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GastoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GastoApiService {

    @POST("/gasto/insertar")
    suspend fun anadirGasto(@Body request: GastoRequest): Response<Unit>

    @GET("/gasto/gastogrupo")
    suspend fun cargarGastosGrupo(@Query("idGrupo") idGrupo: Int?): Response<List<GastoResponse>>
}