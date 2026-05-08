package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UsuarioGrupoApiService {

    @PATCH("/usuarioGrupo/desactivar")
    suspend fun desactivarUsuarioGrupo(@Body grupoRequest : GrupoRequest): Response<Unit>

    @POST("/usuarioGrupo/insertar")
    suspend fun insertarUsuarioAGrupo(@Body grupoRequest : GrupoRequest): Response<Unit>
}