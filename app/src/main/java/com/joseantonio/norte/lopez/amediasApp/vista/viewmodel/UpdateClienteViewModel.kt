package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.entity.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateClienteViewModel() : ViewModel() {

    // LiveData observable del cliente actualizado
    private val _updateUsuarioResult = MutableLiveData<Usuario?>()
    val updateUsuarioResult: LiveData<Usuario?> get() = _updateUsuarioResult

    // Método para actualizar cliente
    fun updateCliente(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
              //  val updatedCliente = updateClienteUseCase(usuario) // Debe devolver Cliente? o null si falla
                withContext(Dispatchers.Main) {
               //     _updateUsuarioResult.value = updatedCliente
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _updateUsuarioResult.value = null // Indicamos fallo
                }
            }
        }
    }
}