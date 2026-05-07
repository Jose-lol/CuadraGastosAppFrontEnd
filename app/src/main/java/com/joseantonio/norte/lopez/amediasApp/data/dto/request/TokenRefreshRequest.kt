package com.joseantonio.norte.lopez.amediasApp.data.dto.request

import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)