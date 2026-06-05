package com.joseantonio.norte.lopez.cuadragastosapp.data.Repository


import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GastoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.SaldoUsuarioGrupo
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.network.RetrofitClient
import retrofit2.Response

class GastoRepository(requireContext: Context)  {

    private val api = RetrofitClient.instanceGasto

    suspend fun anadirGasto(gasto: GastoRequest): Response<Unit>{
        return api.anadirGasto(gasto)
    }

    suspend fun cargarGastosGrupo(idGrupo : Int?): Response<List<GastoResponse>> {
        return api.cargarGastosGrupo(idGrupo)
    }

    suspend fun obtenerCuentasClarasDelGrupo(idGrupo : Int?): Response<List<SaldoUsuarioGrupo>> {
        return api.obtenerCuentasClarasDelGrupo(idGrupo)
    }

    suspend fun saldarCuentasGrupo(idGrupo : Int?): Response<Unit>{
        return api.saldarCuentasGrupo(idGrupo)
    }
}