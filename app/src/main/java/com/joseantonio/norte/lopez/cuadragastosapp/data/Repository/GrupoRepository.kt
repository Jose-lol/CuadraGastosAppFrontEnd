package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class GrupoRepository (requireContext: Context) {
    private val api = RetrofitClient.instanceGrupo

    suspend fun cargarGrupos(idUsuario: Int?): Response<Collection<GrupoResponse>> {
        return api.cargarGrupos(idUsuario)
    }

    suspend fun guardarGrupos(grupoRequest: GrupoRequest): Response<Unit> {
        return api.guardarGrupos(grupoRequest)
    }
}