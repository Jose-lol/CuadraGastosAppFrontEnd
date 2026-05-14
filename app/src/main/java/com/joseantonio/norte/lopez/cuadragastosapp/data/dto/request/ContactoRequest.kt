package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request

import com.google.gson.annotations.SerializedName

data class ContactoRequest(
    @SerializedName("telefono")
    val telefono: String? = null
)