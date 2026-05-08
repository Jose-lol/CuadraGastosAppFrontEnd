package com.joseantonio.norte.lopez.cuadragastosapp.auth.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("contrasena")
    val contrasena: String
)