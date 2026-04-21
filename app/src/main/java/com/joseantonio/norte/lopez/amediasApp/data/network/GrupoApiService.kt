package com.joseantonio.norte.lopez.amediasApp.data.network

import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GrupoApiService {

    @POST("/grupo/cargar")
    suspend fun cargarGrupos(@Body usuario: UsuarioResponse): Response<Collection<GrupoResponse>>

    @POST("/grupo/guardar")
    suspend fun guardarGrupos(@Body grupoRequest : GrupoRequest): Response<Unit>

    @PATCH("grupo/{idGrupo}/desactivar")
    suspend fun desactivarGrupo(@Path("idGrupo") idGrupo: Int?): Response<Unit>

    @PATCH("grupo/{idGrupo}/usuarios/{idUsuario}/desactivar")
    suspend fun desactivarUsuarioGrupo(@Path("idGrupo") idGrupo: Int?): Response<Unit>
}