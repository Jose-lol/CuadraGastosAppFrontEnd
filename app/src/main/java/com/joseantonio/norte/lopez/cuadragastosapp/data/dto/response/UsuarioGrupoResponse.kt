package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import com.google.gson.annotations.SerializedName
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Usuario

data class UsuarioGrupoResponse(

    @SerializedName("grupo")
    val grupo: GrupoResponse ,
    @SerializedName("idUsuario")
    val idUsuario: Int = 0,
    @SerializedName("nombre")
    val nombre: String = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("telefono")
    val telefono: String? = "",
    @SerializedName("rol")
    val rol: String? = ""
)