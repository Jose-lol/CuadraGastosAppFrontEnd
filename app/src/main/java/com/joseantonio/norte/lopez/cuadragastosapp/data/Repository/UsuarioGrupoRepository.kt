package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioGrupoRepository(requireContext: Context) {

    private val api = RetrofitClient.instanceUsuarioGrupo

    suspend fun desactivarUsuarioGrupo(usuarioGrupoRequest : UsuarioGrupoRequest): Response<Unit> {
        return api.desactivarUsuarioGrupo(usuarioGrupoRequest)
    }
    suspend fun insertarUsuarioAGrupo(usuarioGrupoRequest: UsuarioGrupoRequest): Response<Unit> {
        return api.insertarUsuarioAGrupo(usuarioGrupoRequest)
    }

    suspend fun cargarMiembrosGrupo(idGrupo: Int?): Response<List<UsuarioGrupoResponse>>{
        return api.cargarMiembrosGrupo(idGrupo)
    }

    suspend fun hacerAdministrador(usuarioGrupoRequest: UsuarioGrupoRequest): Response<Unit> {
        return api.hacerAdministrador(usuarioGrupoRequest)
    }

    suspend fun cargarMisGrupos(): Response<Collection<UsuarioGrupoResponse>> {
        return api.cargarMisGrupos()
    }

    suspend fun cambiarNombreGrupo(grupoRequest: GrupoRequest): Response<UsuarioGrupoResponse> {
        return api.cambiarNombreGrupo(grupoRequest)
    }
}