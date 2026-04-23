package com.joseantonio.norte.lopez.amediasApp.vista.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import kotlinx.coroutines.launch


class AnadirAmigoAGrupoViewModel(private val repositoryUsuarioGrupo: UsuarioGrupoRepository,private val repositoryUsuario: UsuarioRepository) : ViewModel() {

    private val _resultado = MutableLiveData<String>()
    val resultado: LiveData<String> get() = _resultado

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _listaAmigos = MutableLiveData<Collection<UsuarioResponse>>()

    val listaAmigos  : LiveData<Collection<UsuarioResponse>> = _listaAmigos
    fun anadirAmigoAGrupo(grupoRequest : GrupoRequest) {
        viewModelScope.launch {
            try {
                val response = repositoryUsuarioGrupo.insertarUsuarioAGrupo(grupoRequest)

                if (response.isSuccessful) {
                    _resultado.value = "Éxito: Usuario añadido al grupo con exito"
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

    fun cargarAmigos(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val response = repositoryUsuario.cargarAmigos(idUsuario)

                if (response.isSuccessful) {
                    _listaAmigos.value =response.body()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

}