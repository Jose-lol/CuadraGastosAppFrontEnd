package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.LoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.request.RegistroRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.response.JwtResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class UsuarioRepository(requireContext: Context) {
    private val usuarioApi = RetrofitClient.instanceUsuario

    suspend fun actualizarPerfil(usuario: UsuarioRequest): Response<UsuarioResponse> {
        return usuarioApi.actualizarPerfil(usuario)
    }
}
