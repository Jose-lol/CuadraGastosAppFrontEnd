package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioRequest
import kotlinx.coroutines.launch

class CompletarPerfilViewModel(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _registroExitoso = MutableLiveData<Unit?>()
    val registroExitoso: LiveData<Unit?> = _registroExitoso

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun actualizarPerfil(nombre: String, telefono: String) {
        viewModelScope.launch {
            try {
                val usuarioRequest = UsuarioRequest(nombre = nombre, telefono = telefono)

                val response = usuarioRepository.actualizarPerfil(usuarioRequest)

                if (response.isSuccessful) {
                    _registroExitoso.value = Unit
                } else {
                    _error.value = when (response.code()) {
                        400 -> "Datos incorrectos"
                        401 -> "Sesión expirada"
                        409 -> "Este teléfono ya está registrado por otro usuario"
                        else -> "Error en el servidor: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                Log.e("RETROFIT_DEBUG", "¡Excepción crítica capturada!",e)
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }
}