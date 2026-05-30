package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto
import kotlinx.coroutines.launch

class ConfigurarGruposViewModel(
    private val usuarioGrupoRepository: UsuarioGrupoRepository
) : ViewModel() {

    private val _usuarioGrupo = MutableLiveData<UsuarioGrupoResponse>()
    val usuarioGrupo : LiveData<UsuarioGrupoResponse> = _usuarioGrupo

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _registroExitoso = MutableLiveData<Unit?>()
    val registroExitoso: LiveData<Unit?> = _registroExitoso
    private val _listaMiembros = MutableLiveData<List<UsuarioGrupoResponse>>()
    val listaMiembros: LiveData<List<UsuarioGrupoResponse>> = _listaMiembros

    fun desactivarUsuarioGrupo(usuarioGrupoRequest: UsuarioGrupoRequest) {
        viewModelScope.launch {
            try {
                val response = usuarioGrupoRepository.desactivarUsuarioGrupo(usuarioGrupoRequest)
                if (response.isSuccessful) {
                    _registroExitoso.value = response.body()
                    cargarMiembrosGrupo(usuarioGrupoRequest.idGrupo)
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

    fun cargarMiembrosGrupo(idGrupo: Int?) {
        viewModelScope.launch {
            try {
                val response = usuarioGrupoRepository.cargarMiembrosGrupo(idGrupo)
                if (response.isSuccessful) {
                    _listaMiembros.value = response.body()
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

    fun hacerAdministrador(usuarioGrupoRequest : UsuarioGrupoRequest) {
        viewModelScope.launch {
            try {
                val response = usuarioGrupoRepository.hacerAdministrador(usuarioGrupoRequest)
                if (response.isSuccessful) {
                    _registroExitoso.value = response.body()
                    cargarMiembrosGrupo(usuarioGrupoRequest.idGrupo)
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

    fun cambiarNombreGrupo(grupoRequest: GrupoRequest){
        viewModelScope.launch {
            try {
                val response = usuarioGrupoRepository.cambiarNombreGrupo(grupoRequest)
                if (response.isSuccessful) {
                    _usuarioGrupo.value = response.body()
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