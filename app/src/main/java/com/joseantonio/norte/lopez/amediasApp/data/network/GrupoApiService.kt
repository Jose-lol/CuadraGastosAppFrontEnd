package com.joseantonio.norte.lopez.amediasApp.data.network

import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GrupoApiService {

    @POST("/grupo/cargar")
    suspend fun cargarGrupos(@Body usuario: UsuarioResponse): Response<Collection<GrupoResponse>>

    @POST("/grupo/guardar")
    suspend fun guardarGrupos(@Body grupoRequest : GrupoRequest): Response<Unit>

    @POST("/grupo/eliminar")
    suspend fun eliminarGrupos(@Body idGrupo : Int?): Response<Unit>
}