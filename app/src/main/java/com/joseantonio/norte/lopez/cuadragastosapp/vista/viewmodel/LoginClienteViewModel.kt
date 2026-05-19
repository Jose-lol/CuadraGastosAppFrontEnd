package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GoogleLoginRequest
import com.joseantonio.norte.lopez.cuadragastosapp.auth.response.JwtResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import kotlinx.coroutines.launch

class LoginClienteViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _usuario = MutableLiveData<JwtResponse>()
    val usuario: LiveData<JwtResponse?> = _usuario

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.login(email, contrasena)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        sessionManager.saveSession(body.accessToken, body.refreshToken, body.usuario)
                        _usuario.value=response.body()
                    }
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
                _error.value = "No se pudo conectar con el servidor."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginWithGoogle(token : GoogleLoginRequest){

            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null

                try {
                    val response = repository.loginWithGoogle(token)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            sessionManager.saveSession(
                                body.accessToken,
                                body.refreshToken,
                                body.usuario
                            )
                            _usuario.value = response.body()
                        }
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
                    _error.value = "No se pudo conectar con el servidor."
                } finally {
                    _isLoading.value = false
                }
            }
    }

}

