package com.joseantonio.norte.lopez.amediasApp.data.network

import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UsuarioGrupoApiService {

    @PATCH("usuariogrupo/{idGrupo}/usuarios/{idUsuario}/desactivar")
    suspend fun desactivarUsuarioGrupo(@Path("idGrupo") idGrupo: Int?,
                                       @Path("idUsuario") idUsuario: Int): Response<Unit>
}