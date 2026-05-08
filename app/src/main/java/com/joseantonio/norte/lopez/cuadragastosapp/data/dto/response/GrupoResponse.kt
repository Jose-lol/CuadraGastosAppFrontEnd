package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import com.google.gson.annotations.SerializedName
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Grupo

data class GrupoResponse (

    @SerializedName("idGrupo")
    val idGrupo: Int? = 0,

    @SerializedName("nombre")
    val nombre: String? = "",

    @SerializedName("estado")
    val estado: String? = ""
) {
    companion object {
        fun fromEntity(grupo: Grupo): GrupoResponse {
            return GrupoResponse(
                idGrupo = grupo.id,
                nombre = grupo.nombre,
                estado = grupo.estado
            )
        }
    }
}