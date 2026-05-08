package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository

class DetalleGruposViewModel  (private val repository: GrupoRepository) : ViewModel() {

    private val _grupo = MutableLiveData<Unit>()
    val grupo: LiveData<Unit> = _grupo

    // Para mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


}