package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import kotlinx.coroutines.launch

class LoginClienteViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Para los datos del usuario
    private val _usuario = MutableLiveData<UsuarioResponse?>()
    val usuario: LiveData<UsuarioResponse?> = _usuario

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, contrasena)
                if (response.isSuccessful) {
                    _usuario.value = response.body()
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }
}

