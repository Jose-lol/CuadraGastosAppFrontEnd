package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.ContactoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response
import retrofit2.http.Body

class ContactoRepository(requireContext: Context)  {

    private val api = RetrofitClient.instanceContacto

    suspend fun anadirContacto(contacto: ContactoRequest): Response<Unit>{
        return api.anadirContacto(contacto)
    }

    suspend fun eliminarContacto(contacto : ContactoRequest): Response<Unit>{
        return api.eliminarContacto(contacto)
    }

    suspend fun cargarContactos(): Response<Collection<UsuarioResponse>> {
        return api.cargarContactos()
    }

}