package com.joseantonio.norte.lopez.cuadragastosapp.auth.response

import com.google.gson.annotations.SerializedName
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse

class JwtResponse {
    @SerializedName("accessToken")
    val accessToken: String? = null
    @SerializedName("refreshToken")
    val refreshToken: String? = null

    @SerializedName("usuario")
    val usuario: UsuarioResponse? = null
}