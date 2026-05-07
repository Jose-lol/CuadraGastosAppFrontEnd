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

    fun cargarAmigos(idUsuario: Int?) {
        viewModelScope.launch {
            try {
                val response = repositoryUsuario.cargarAmigos(idUsuario)

                if (response.isSuccessful) {
                    _listaAmigos.value =response.body()
                } else {
                    _error.value = when (response.code()) {

                        400 -> "Solicitud incorrecta"

                        401 -> "Sesión expirada. Vuelve a iniciar sesión"

                        403 -> "No tienes permisos para acceder"

                        404 -> "No se encontraron grupos"

                        408 -> "Tiempo de espera agotado"

                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"

                        500 -> "Error interno del servidor"

                        502 -> "Servidor no disponible"

                        503 -> "Servicio temporalmente fuera de servicio"

                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

}