package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import kotlinx.coroutines.launch

class CargarGruposViewModel (private val repository: GrupoRepository) : ViewModel() {

    // Para los datos del grupo
    private val _grupo = MutableLiveData<Collection<GrupoResponse>>()
    val grupo : LiveData<Collection<GrupoResponse>> = _grupo

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarGrupos(idUsuario: Int?) {
        viewModelScope.launch {
            try {
                val response = repository.cargarGrupos(idUsuario)
                if (response.isSuccessful) {
                    _grupo.value = response.body()
                } else {
                    _error.value = when (response.code()) {

                        400 -> "Solicitud incorrecta"

                        401 -> "Sesión expirada. Vuelve a iniciar sesión"

                        403 -> "No tienes permisos para acceder"

                        404 -> "No se encontraron grupos"

                        408 -> "Tiempo de espera agotado"

                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"

                        500 -> "Error interno del servidor"

                        502 -> "Servidor no disponible"

                        503 -> "Servicio temporalmente fuera de servicio"

                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: "+e.message.toString()
            }
        }
    }
}