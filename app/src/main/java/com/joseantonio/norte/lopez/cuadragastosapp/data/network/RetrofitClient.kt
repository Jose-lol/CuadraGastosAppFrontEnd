package com.joseantonio.norte.lopez.cuadragastosapp.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.joseantonio.norte.lopez.cuadragastosapp.auth.AuthInterceptor
import com.joseantonio.norte.lopez.cuadragastosapp.auth.TokenAuthenticator
import com.joseantonio.norte.lopez.cuadragastosapp.auth.TokenHolder
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Volatile
    private var retrofit: Retrofit? = null

    private lateinit var sessionManager: SessionManager

    private val tokenHolder = TokenHolder()

    private val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
        LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    fun init(context: Context) {

        if (retrofit != null) return

        synchronized(this) {

            if (retrofit != null) return

            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
                .create()

            val appContext = context.applicationContext
            sessionManager = SessionManager(appContext)

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(sessionManager))
                .authenticator(
                    TokenAuthenticator(
                        sessionManager,
                        { authApiService },
                        tokenHolder
                    )
                )
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }

    //IMPORTANTE: authApi fuera del mismo ciclo crítico
    val authApiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
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
    val instanceContacto: ContactoApiService by lazy {
        createService(ContactoApiService::class.java)
    }

    val instanceGasto: GastoApiService by lazy {
        createService(GastoApiService::class.java)
    }

}
