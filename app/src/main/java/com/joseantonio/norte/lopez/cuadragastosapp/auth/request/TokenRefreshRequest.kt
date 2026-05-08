package com.joseantonio.norte.lopez.cuadragastosapp.auth.request

import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)