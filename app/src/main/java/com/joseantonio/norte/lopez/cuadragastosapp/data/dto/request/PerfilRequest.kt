package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request

import com.google.gson.annotations.SerializedName

data class PerfilRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("telefono")
    val telefono: String
)