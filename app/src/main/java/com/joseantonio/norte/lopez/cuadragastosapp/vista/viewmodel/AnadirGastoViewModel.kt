package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GastoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GastoRequest
import kotlinx.coroutines.launch

class AnadirGastoViewModel  (private val repository: GastoRepository) : ViewModel() {

    private val _resultado = MutableLiveData<String>()

    val resultado: LiveData<String> get() = _resultado

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun anadirGasto(gastoRequest: GastoRequest) {

        viewModelScope.launch {

            try {

                val response = repository.anadirGasto(gastoRequest)

                if (response.isSuccessful) {
                    _resultado.value = "Éxito: gasto añadido"
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
                _error.value = e.message
            }
        }
    }
}