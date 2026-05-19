package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UsuarioGrupoApiService {

    @PATCH("/usuarioGrupo/desactivar")
    suspend fun desactivarUsuarioGrupo(@Body usuarioGrupoRequest : UsuarioGrupoRequest): Response<Unit>

    @POST("/usuarioGrupo/insertar")
    suspend fun insertarUsuarioAGrupo(@Body usuarioGrupoRequest : UsuarioGrupoRequest): Response<Unit>

    @GET("/usuarioGrupo/cargar")
    suspend fun cargarMiembrosGrupo(@Query("idGrupo") idGrupo: Int?): Response<List<UsuarioGrupoResponse>>

    @PATCH("/usuarioGrupo/hacerAdministrador")
    suspend fun hacerAdministrador(@Body usuarioGrupoRequest: UsuarioGrupoRequest): Response<Unit>

    @GET("/usuarioGrupo/cargarMisGrupos")
    suspend fun cargarMisGrupos(): Response<Collection<UsuarioGrupoResponse>>

    @PATCH("/usuarioGrupo/cambiarNombreGrupo")
    suspend fun cambiarNombreGrupo(@Body grupoRequest: GrupoRequest): Response<UsuarioGrupoResponse>

}