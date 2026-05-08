package com.joseantonio.norte.lopez.cuadragastosapp.auth.request

import com.google.gson.annotations.SerializedName

data class RegistroRequest (
    @SerializedName("email")
    val email: String,
    @SerializedName("contrasena")
    val contrasena: String
)