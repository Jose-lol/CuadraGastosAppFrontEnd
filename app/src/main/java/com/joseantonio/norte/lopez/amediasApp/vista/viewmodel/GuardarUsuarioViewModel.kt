package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuardarUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _resultado = MutableLiveData<String>()
    val resultado: LiveData<String> get() = _resultado

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    fun registrarUsuario(email: String, pass: String) {

        viewModelScope.launch {
            try {
                val response = repository.registrar(email, pass)

                if (response.isSuccessful) {
                    _resultado.value = "Éxito: Usuario creado con ID ${response.body()?.idUsuario}"
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
                _resultado.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }
}