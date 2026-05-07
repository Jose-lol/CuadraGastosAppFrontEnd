package com.joseantonio.norte.lopez.amediasApp.auth

import android.content.SharedPreferences
import android.util.Log
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.TokenRefreshRequest
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.data.network.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val sessionManager: SessionManager,
    private val apiProvider: () -> AuthApi,
    private val tokenHolder: TokenHolder
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if (responseCount(response) >= 2) {
            sessionManager.clearSession()
            return null
        }

        val currentToken = sessionManager.getAccessToken()
        val requestToken =
            response.request.header("Authorization")?.removePrefix("Bearer ")

        if (currentToken != null && currentToken != requestToken) {
            Log.d("AUTH", "♻️ Token ya actualizado → reintentando request")
            return response.request.newBuilder()
                .header("Authorization", "Bearer $currentToken")
                .build()
        }

        val refreshToken = sessionManager.getRefreshToken()
            ?: run {
                Log.d("AUTH", "❌ No refresh token → logout")
                sessionManager.clearSession()
                return null
            }

        //CONTROL GLOBAL DE REFRESH
        if (!tokenHolder.startRefreshing()) {
            Log.d("AUTH", "⏳ Otro refresh en curso → esperando resultado")
            return null
        }

        return try {
            Log.d("AUTH", "📡 Llamando backend refresh...")
            val refreshResponse = runBlocking {
                apiProvider().refrescarToken(
                    TokenRefreshRequest(refreshToken)
                )
            }

            if (refreshResponse.isSuccessful) {

                val body = refreshResponse.body()

                if (body != null) {


                    Log.d("AUTH", "✅ REFRESH OK")

                    sessionManager.saveSession(
                        body.accessToken,
                        body.refreshToken,
                        body.idUsuario
                    )

                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${body.accessToken}")
                        .build()
                }
            }
            Log.d("AUTH", "❌ Refresh FAIL (${refreshResponse.code()})")
            sessionManager.clearSession()
            null

        } catch (e: Exception) {
            Log.e("AUTH", "💥 Refresh exception", e)
            sessionManager.clearSession()
            null

        } finally {
            Log.d("AUTH", "🔓 Refresh lock liberado")
            tokenHolder.finishRefreshing()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse

        while (prior != null) {
            count++
            prior = prior.priorResponse
        }

        return count
    }
}