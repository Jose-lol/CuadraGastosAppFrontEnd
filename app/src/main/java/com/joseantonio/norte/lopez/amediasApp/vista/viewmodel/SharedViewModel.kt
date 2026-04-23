package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val _grupoSeleccionado = MutableStateFlow<GrupoResponse?>(null)
    val grupoSeleccionado = _grupoSeleccionado.asStateFlow()

    fun seleccionarGrupo(grupo: GrupoResponse) {
        _grupoSeleccionado.value = grupo
    }

}