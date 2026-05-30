package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GastoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GastoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import kotlinx.coroutines.launch

class DetalleGruposViewModel  (private val repository: GastoRepository) : ViewModel() {

        private val _gastoGrupo = MutableLiveData<List<GastoResponse>>()

        val gastoGrupo : LiveData<List<GastoResponse>> = _gastoGrupo
        private val _error = MutableLiveData<String?>()
        val error: LiveData<String?> = _error

        fun  cargarGastosGrupo(idGrupo: Int?){
            viewModelScope.launch {
                try {
                    val response = repository.cargarGastosGrupo(idGrupo)
                    if (response.isSuccessful) {
                        _gastoGrupo.value = response.body()
                    } else {
                        _error.value = when (response.code()) {
                            400 -> "Solicitud incorrecta"
                            401 -> "Sesión expirada. Vuelve a iniciar sesión"
                            403 -> "No tienes permisos para acceder"
                            404 -> "No se encontraron grupos"
                            408 -> "Tiempo de espera agotado"
                            429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                            else -> "Error inesperado: ${response.code()}"
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RETROFIT_DEBUG", "¡Excepción crítica capturada!",e)
                    _error.value = "Error de conexión "+e.message.toString()
                }
            }
        }
    }