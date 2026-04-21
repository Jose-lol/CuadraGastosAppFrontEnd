package com.joseantonio.norte.lopez.amediasApp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioGrupoRepository(requireContext: Context) {

    private val api = RetrofitClient.instanceUsuarioGrupo

    suspend fun desactivarUsuarioGrupo(idGrupo: Int?, idUsuario: Int): Response<Unit> {
        return api.desactivarUsuarioGrupo(idGrupo,idUsuario)
    }


}