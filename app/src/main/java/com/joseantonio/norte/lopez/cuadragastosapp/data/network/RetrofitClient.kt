package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import android.content.Context
import com.joseantonio.norte.lopez.cuadragastosapp.auth.AuthInterceptor
import com.joseantonio.norte.lopez.cuadragastosapp.auth.TokenAuthenticator
import com.joseantonio.norte.lopez.cuadragastosapp.auth.TokenHolder
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Volatile
    private var retrofit: Retrofit? = null

    private lateinit var sessionManager: SessionManager

    private val tokenHolder = TokenHolder()

    fun init(context: Context) {

        if (retrofit != null) return

        synchronized(this) {

            if (retrofit != null) return

            val appContext = context.applicationContext
            sessionManager = SessionManager(appContext)

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(sessionManager))
                .authenticator(
                    TokenAuthenticator(
                        sessionManager,
                        { authApi }, // OK ahora
                        tokenHolder
                    )
                )
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    //IMPORTANTE: authApi fuera del mismo ciclo crítico
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    private fun <T> createService(serviceClass: Class<T>): T {
        return retrofit?.create(serviceClass)
            ?: throw IllegalStateException("Retrofit no inicializado")
    }

    val instanceUsuario: UsuarioApiService by lazy {
        createService(UsuarioApiService::class.java)
    }

    val instanceGrupo: GrupoApiService by lazy {
        createService(GrupoApiService::class.java)
    }

    val instanceUsuarioGrupo: UsuarioGrupoApiService by lazy {
        createService(UsuarioGrupoApiService::class.java)
    }
}
