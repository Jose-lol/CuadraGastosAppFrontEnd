package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import kotlinx.coroutines.launch

class CuentaClienteViewModel(private val repository: AuthRepository, private val sessionManager: SessionManager): ViewModel()  {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun logout(token: String?) {

        viewModelScope.launch {
            _error.value = null
            try {
                val response = repository.logout(token)
                if (response.isSuccessful) {
                    sessionManager.clearSession()
                } else {
                    //si falla backend limpiamos sesión igual
                    sessionManager.clearSession()

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

                sessionManager.clearSession()
                _error.value = "No se pudo conectar con el servidor."

            }
        }
    }
}