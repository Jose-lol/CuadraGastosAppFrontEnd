package com.joseantonio.norte.lopez.amediasApp.vista.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.ConfigurarGruposViewModel

class ConfigurarGruposViewModelFactory(
    private val grupoRepository: GrupoRepository,
    private val usuarioGrupoRepository: UsuarioGrupoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigurarGruposViewModel(
            grupoRepository,
            usuarioGrupoRepository
        ) as T
    }
}