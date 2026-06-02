package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.ActividadResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Actividad
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ActividadApiService {

    @GET("/actividad/listaactividades")
    suspend fun misActividades(): Response<List<ActividadResponse>>
}