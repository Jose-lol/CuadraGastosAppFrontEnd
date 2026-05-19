package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ContactoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import kotlinx.coroutines.launch


class AnadirAmigoAGrupoViewModel(private val repositoryUsuarioGrupo: UsuarioGrupoRepository,private val repositoryContacto: ContactoRepository) : ViewModel() {

    private val _resultado = MutableLiveData<String>()
    val resultado: LiveData<String> get() = _resultado

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _listaAmigos = MutableLiveData<Collection<UsuarioResponse>>()

    val listaAmigos  : LiveData<Collection<UsuarioResponse>> = _listaAmigos

    fun anadirAmigoAGrupo(usuarioGrupoRequest : UsuarioGrupoRequest) {
        viewModelScope.launch {
            try {
                val response = repositoryUsuarioGrupo.insertarUsuarioAGrupo(usuarioGrupoRequest)

                if (response.isSuccessful) {
                    _resultado.value = "Éxito: Usuario añadido al grupo con exito"
                    cargarContactosNoEnGrupo(usuarioGrupoRequest.idGrupo)
                } else {
                    _error.value = when (response.code()) {
                        400 -> "Solicitud incorrecta"
                        401 -> "Sesión expirada. Vuelve a iniciar sesión"
                        403 -> "No tienes permisos para acceder"
                        404 -> "No se encontraron grupos"
                        408 -> "Tiempo de espera agotado"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

    fun cargarContactosNoEnGrupo(idGrupo: Int?) {
        viewModelScope.launch {
            try {
                val response = repositoryContacto.cargarContactosNoEnGrupo(idGrupo)

                if (response.isSuccessful) {
                    _listaAmigos.value =response.body()?: emptyList()
                } else {
                    _error.value = when (response.code()) {
                        400 -> "Solicitud incorrecta"
                        401 -> "Sesión expirada. Vuelve a iniciar sesión"
                        403 -> "No tienes permisos para acceder"
                        404 -> "No se encontraron grupos"
                        408 -> "Tiempo de espera agotado"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

}