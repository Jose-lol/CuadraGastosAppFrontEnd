package com.joseantonio.norte.lopez.amediasApp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import retrofit2.Response

class GrupoRepository (requireContext: Context) {
    private val api = RetrofitClient.instanceGrupo

    suspend fun cargarGrupos(usuario: UsuarioResponse): Response<Collection<GrupoResponse>> {
        return api.cargarGrupos(usuario)
    }

    suspend fun guardarGrupos(grupoRequest: GrupoRequest): Response<Unit> {
        return api.guardarGrupos(grupoRequest)
    }

    suspend fun eliminarGrupos(idGrupo  : Int?): Response<Unit> {
        return api.desactivarGrupo(idGrupo)
    }
}