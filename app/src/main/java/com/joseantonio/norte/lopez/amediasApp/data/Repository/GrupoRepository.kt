package com.joseantonio.norte.lopez.amediasApp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import retrofit2.Response

class GrupoRepository (requireContext: Context) {
    private val api = RetrofitClient.instanceGrupo

    suspend fun cargarGrupos(idUsuario : Int): Response<Collection<GrupoResponse>> {
        return api.cargarGrupos(idUsuario)
    }

    suspend fun guardarGrupos(idUsuario : Int, grupoRequest: GrupoRequest): Response<Unit> {
        return api.guardarGrupos(idUsuario,grupoRequest)
    }

    suspend fun eliminarGrupos(idGrupo  : Int?): Response<Unit> {
        return api.desactivarGrupo(idGrupo)
    }
}