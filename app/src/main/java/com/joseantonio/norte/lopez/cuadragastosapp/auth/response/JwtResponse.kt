package com.joseantonio.norte.lopez.cuadragastosapp.auth.response

import com.google.gson.annotations.SerializedName

class JwtResponse {
    @SerializedName("accessToken")
    val accessToken: String? = null
    @SerializedName("refreshToken")
    val refreshToken: String? = null
    @SerializedName("email")
    val email: String? = null
    @SerializedName("idUsuario")
    val idUsuario: Int? = null
}