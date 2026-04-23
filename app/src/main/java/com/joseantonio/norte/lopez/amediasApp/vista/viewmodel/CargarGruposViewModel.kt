package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import kotlinx.coroutines.launch

class CargarGruposViewModel (private val repository: GrupoRepository) : ViewModel() {

    // Para los datos del grupo
    private val _grupo = MutableLiveData<Collection<GrupoResponse>>()
    val grupo : LiveData<Collection<GrupoResponse>> = _grupo

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarGrupos(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val response = repository.cargarGrupos(idUsuario)
                if (response.isSuccessful) {
                    _grupo.value = response.body()
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: "+e.message.toString()
            }
        }
    }
}