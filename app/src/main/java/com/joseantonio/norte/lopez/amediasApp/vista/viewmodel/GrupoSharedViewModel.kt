package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.ViewModel
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GrupoSharedViewModel : ViewModel() {
    // Usamos un StateFlow o LiveData para que los fragmentos reaccionen a cambios
    private val _grupoSeleccionado = MutableStateFlow<GrupoResponse?>(null)
    val grupoSeleccionado = _grupoSeleccionado.asStateFlow()

    fun seleccionarGrupo(grupo: GrupoResponse) {
        _grupoSeleccionado.value = grupo
    }

}