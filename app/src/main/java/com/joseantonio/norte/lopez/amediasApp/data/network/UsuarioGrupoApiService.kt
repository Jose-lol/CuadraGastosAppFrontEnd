package com.joseantonio.norte.lopez.amediasApp.data.network

import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioGrupoApiService {

    @PATCH("/usuarioGrupo/desactivar")
    suspend fun desactivarUsuarioGrupo(@Body grupoRequest : GrupoRequest): Response<Unit>

    @POST("/usuarioGrupo/insertar")
    suspend fun insertarUsuarioAGrupo(@Body grupoRequest : GrupoRequest): Response<Unit>
}