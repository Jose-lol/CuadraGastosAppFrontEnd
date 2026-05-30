package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import kotlinx.coroutines.launch


class ModificarPerfilViewModel(
    private val repository: UsuarioRepository,
    private val sessionManager: SessionManager,
    ) : ViewModel() {

    private val _updateUsuarioResult = MutableLiveData<UsuarioResponse?>()
    val updateUsuarioResult: LiveData<UsuarioResponse?> get() = _updateUsuarioResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun modificarUsuario(usuario: UsuarioRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.actualizarPerfil(usuario)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _updateUsuarioResult.value = body
                        sessionManager.updateSession(body)
                    }
                } else {
                    _error.value = when (response.code()) {
                        400 -> "Solicitud incorrecta"
                        401 -> "Sesión expirada. Vuelve a iniciar sesión"
                        403 -> "No tienes permisos para acceder"
                        404 -> "No se encontraron usuarios"
                        408 -> "Tiempo de espera agotado"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"

                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                Log.e("RETROFIT_DEBUG", "¡Excepción crítica capturada!",e)
                _error.value = "No se pudo conectar con el servidor."
            } finally {
                _isLoading.value = false
            }
        }
    }
}