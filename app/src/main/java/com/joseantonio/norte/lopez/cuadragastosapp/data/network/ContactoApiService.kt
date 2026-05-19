package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.ContactoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query


interface ContactoApiService {

    @POST("/contacto/agregar")
    suspend fun anadirContacto(@Body request: ContactoRequest): Response<Unit>

    @HTTP(method = "DELETE", path = "/contacto/eliminar", hasBody = true)
    suspend fun eliminarContacto(@Body request: ContactoRequest): Response<Unit>

    @GET("/contacto/cargar")
    suspend fun cargarContactos(): Response<Collection<UsuarioResponse>>

    @GET("/contacto/cargarNoEnGrupo")
    suspend fun cargarContactosNoEnGrupo(@Query("idGrupo") idGrupo: Int?): Response<Collection<UsuarioResponse>>
}