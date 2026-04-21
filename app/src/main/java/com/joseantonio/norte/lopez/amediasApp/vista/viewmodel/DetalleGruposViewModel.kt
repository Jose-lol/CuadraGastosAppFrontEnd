package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import kotlinx.coroutines.launch

class DetalleGruposViewModel  (private val repository: GrupoRepository) : ViewModel() {

    private val _grupo = MutableLiveData<Unit>()
    val grupo: LiveData<Unit> = _grupo

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun crearGrupo(grupoRequest: GrupoRequest) {
        viewModelScope.launch {
            try {
                val response = repository.guardarGrupos(grupoRequest)
                if (response.isSuccessful) {
                    _grupo.value = response.body()
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión " + e.message.toString()
            }
        }
    }
}