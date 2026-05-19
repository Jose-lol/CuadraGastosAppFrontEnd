package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import com.google.gson.annotations.SerializedName
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Usuario

data class UsuarioResponse(
    @SerializedName("idUsuario")
    val idUsuario: Int = 0,
    @SerializedName("nombre")
    val nombre: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("telefono")
    val telefono: String? = ""
) {

    fun toContacto(agregado: Boolean = false): Contacto {
        return Contacto(
            nombre = this.nombre ?: "Desconocido",
            telefono = this.telefono ?: "",
            estaAgregado = agregado
        )
    }

    companion object {
        fun fromEntity(usuario: Usuario): UsuarioResponse {
            return UsuarioResponse(
                idUsuario = usuario.id,
                nombre = usuario.nombre,
                email = usuario.email,
                telefono = usuario.telefono
            )
        }
    }
}