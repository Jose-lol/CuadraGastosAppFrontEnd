package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import kotlinx.coroutines.launch

class ConfigurarGruposViewModel (private val repository: GrupoRepository) : ViewModel() {

    // Para los datos del grupo
    private val _grupo = MutableLiveData<Unit>()
    val grupo : LiveData<Unit> = _grupo

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun  eliminarGrupo(idGrupo : Int?) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarGrupos(idGrupo)
                if (response.isSuccessful) {
                    _grupo.value = response.body()
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión "+e.message.toString()
            }
        }
    }
}