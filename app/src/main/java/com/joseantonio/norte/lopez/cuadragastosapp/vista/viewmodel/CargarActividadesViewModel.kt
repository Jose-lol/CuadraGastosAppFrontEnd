package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ActividadRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GastoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.ActividadResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Actividad
import kotlinx.coroutines.launch

class CargarActividadesViewModel (private val repository: ActividadRepository) : ViewModel()  {

    private val _actividad = MutableLiveData<List<ActividadResponse>>()
    val actividad : LiveData<List<ActividadResponse>> = _actividad

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun misActividades() {
        viewModelScope.launch {
            try {
                val response = repository.misActividades()
                if (response.isSuccessful) {
                    _actividad.value = response.body()
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
                _error.value = "Error de conexión: "+e.message.toString()
            }
        }
    }


}