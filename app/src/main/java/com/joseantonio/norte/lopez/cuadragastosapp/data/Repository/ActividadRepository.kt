package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.ActividadResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Actividad
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class ActividadRepository(requireContext: Context) {

    private val api = RetrofitClient.instanceActividad

    suspend fun misActividades(): Response<List<ActividadResponse>> {
        return api.misActividades()
    }
}