package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioGrupoRepository(requireContext: Context) {

    private val api = RetrofitClient.instanceUsuarioGrupo

    suspend fun desactivarUsuarioGrupo(grupoRequest : GrupoRequest): Response<Unit> {
        return api.desactivarUsuarioGrupo(grupoRequest)
    }
    suspend fun insertarUsuarioAGrupo(grupoRequest: GrupoRequest): Response<Unit> {
        return api.insertarUsuarioAGrupo(grupoRequest)
    }

}