package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.ViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {

    private val _grupoSeleccionado = MutableStateFlow<GrupoResponse?>(null)
    val grupoSeleccionado = _grupoSeleccionado.asStateFlow()

    fun seleccionarGrupo(grupo: GrupoResponse) {
        _grupoSeleccionado.value = grupo
    }

}