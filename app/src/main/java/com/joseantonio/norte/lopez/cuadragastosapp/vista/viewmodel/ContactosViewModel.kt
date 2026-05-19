package com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ContactoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.ContactoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactosViewModel(private val repository: ContactoRepository
) : ViewModel() {

    // Lista de amigos ya guardados en la App
    private val _contactosApp = MutableLiveData<List<Contacto>>()
    val contactosApp: LiveData<List<Contacto>> = _contactosApp

    // Lista de contactos del móvil que NO son amigos todavía
    private val _contactosMovil = MutableLiveData<List<Contacto>>()
    val contactosMovil: LiveData<List<Contacto>> = _contactosMovil

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var setNumerosAmigos = setOf<String>()

    fun sincronizarContactosMovil(context: Context) {
        viewModelScope.launch {
            try {
                val delMovil = withContext(Dispatchers.IO) {
                    leerContactosLocal(context)
                }

                val listaSoloMovil = delMovil.filter { contactoLocal ->
                    val telLimpio = limpiarTelefono(contactoLocal.telefono)
                    !setNumerosAmigos.contains(telLimpio)
                }.map { contacto ->
                    contacto.copy(telefono = limpiarTelefono(contacto.telefono))
                }
                    .distinctBy { it.telefono }
                    .sortedBy { it.nombre }.distinctBy { limpiarTelefono(it.telefono) }

                _contactosMovil.postValue(listaSoloMovil)

            } catch (e: Exception) {
                _error.postValue("Error al leer contactos: ${e.message}")
            }
        }
    }

    fun eliminarContacto(contacto: ContactoRequest) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarContacto(contacto)
                if (response.isSuccessful) {
                    val listaActual = _contactosApp.value?.toMutableList()
                    listaActual?.removeAll { it.telefono == contacto.telefono }
                    _contactosApp.postValue(listaActual ?: emptyList())
                } else {
                    _error.value = when (response.code()) {
                        401 -> "Sesión expirada"
                        403 -> "No tienes permiso para borrar este contacto"
                        404 -> "El contacto no existe en tu lista"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                        else -> "Error al eliminar: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun cargarAmigosApp() {
        viewModelScope.launch {
            try {
                val response = repository.cargarContactos()

                if (response.isSuccessful) {
                    // Mapeamos la lista de la respuesta a Contactos marcados como agregados
                    val amigos = response.body()?.map { usuarioResponse ->
                        usuarioResponse.toContacto(agregado = true)
                    } ?: emptyList()

                    _contactosApp.postValue(amigos)
                }else{
                    _error.value = when (response.code()) {
                        400 -> "Solicitud incorrecta"
                        401 -> "Sesión expirada. Vuelve a iniciar sesión"
                        403 -> "No tienes permisos para acceder"
                        404 -> "no se pueden cargar contactos de la app"
                        408 -> "Tiempo de espera agotado"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    private fun leerContactosLocal(context: Context): List<Contacto> {
        val temp = mutableListOf<Contacto>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(nameIdx) ?: "Desconocido"
                val tel = cursor.getString(numIdx)
                if (tel != null) temp.add(Contacto(nombre, tel))
            }
        }
        return temp
    }

    private fun limpiarTelefono(tel: String?): String {
        return tel?.replace(Regex("[^0-9]"), "")?.takeLast(9) ?: ""
    }

    fun anadirContacto(contacto: ContactoRequest) {
        viewModelScope.launch {
            try {
                val response = repository.anadirContacto(contacto)
                if (response.isSuccessful) {
                    cargarAmigosApp()
                } else {
                    _error.value = when (response.code()) {
                        400 -> "Solicitud incorrecta"
                        401 -> "Sesión expirada. Vuelve a iniciar sesión"
                        403 -> "No tienes permisos para acceder"
                        404 -> "el usuario no esta registrado en la aplicacion"
                        408 -> "Tiempo de espera agotado"
                        429 -> "Demasiadas solicitudes. Inténtalo más tarde"
                        else -> "Error inesperado: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }
}
