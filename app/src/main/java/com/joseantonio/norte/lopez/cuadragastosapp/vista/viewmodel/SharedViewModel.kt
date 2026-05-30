package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.ViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {


    // --- Estado del Grupo Seleccionado ---
    private val _miUsuarioEnGrupo = MutableStateFlow<UsuarioGrupoResponse?>(null)
    val miUsuarioEnGrupo = _miUsuarioEnGrupo.asStateFlow()
    fun seleccionarGrupo(usuarioGrupo: UsuarioGrupoResponse) {
        _miUsuarioEnGrupo.value = usuarioGrupo
    }

    // --- Estado del Gasto Seleccionado ---
    private val _gastoSeleccionado = MutableStateFlow<GastoResponse?>(null)
    val gastoSeleccionado = _gastoSeleccionado.asStateFlow()

    fun seleccionarGasto(gasto: GastoResponse) {
        _gastoSeleccionado.value = gasto
    }
}