package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuardarUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _resultado = MutableLiveData<String>()
    val resultado: LiveData<String> get() = _resultado

    fun registrarUsuario(email: String, pass: String) {

        viewModelScope.launch {
            try {
                val response = repository.registrar(email, pass)

                if (response.isSuccessful) {
                    _resultado.value = "Éxito: Usuario creado con ID ${response.body()?.idUsuario}"
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _resultado.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _resultado.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }
}