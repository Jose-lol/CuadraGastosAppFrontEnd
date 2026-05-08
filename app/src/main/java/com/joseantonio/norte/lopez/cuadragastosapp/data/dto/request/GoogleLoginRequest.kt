package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request

import com.google.gson.annotations.SerializedName


data class GoogleLoginRequest(
    @SerializedName("token")
    val token: String
)