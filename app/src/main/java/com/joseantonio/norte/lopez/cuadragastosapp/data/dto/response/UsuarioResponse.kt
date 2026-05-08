package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import com.google.gson.annotations.SerializedName
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Usuario

data class UsuarioResponse(
    @SerializedName("idUsuario")
    val idUsuario: Int = 0,

    @SerializedName("nombre")
    val nombre: String?= "",

    @SerializedName("email")
    val email: String? = ""
) {
    companion object {
        fun fromEntity(usuario: Usuario): UsuarioResponse {
            return UsuarioResponse(
                idUsuario = usuario.id,
                nombre = usuario.nombre,
                email = usuario.email
            )
        }
    }
}