package com.joseantonio.norte.lopez.cuadragastosapp.auth

import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        sessionManager.getAccessToken()?.let {
            request.header("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}