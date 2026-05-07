package com.joseantonio.norte.lopez.amediasApp.vista

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.TokenRefreshRequest
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.data.network.RetrofitClient
import com.joseantonio.norte.lopez.amediasApp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)

        RetrofitClient.init(this)
        sessionManager = SessionManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        val token = sessionManager.getAccessToken()

        lifecycleScope.launch {

            val isValidSession = validateSession()

            navGraph.setStartDestination(
                if (isValidSession) {
                    R.id.fragmento_grupo_principal
                } else {
                    R.id.fragmento_login
                }
            )

            navController.setGraph(navGraph, null)

            binding.bottomNavigationView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bottomNavigationView.menu.findItem(destination.id)?.isChecked = true

                binding.bottomNavigationView.visibility =
                    if (destination.id == R.id.fragmento_login ||
                        destination.id == R.id.fragmento_registro
                    ) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // VALIDACIÓN REAL CONTRA BACKEND
    private suspend fun validateSession(): Boolean {

        val refreshToken = sessionManager.getRefreshToken()
            ?: return false

        return try {

            val response = RetrofitClient.authApi.refrescarToken(
                TokenRefreshRequest(refreshToken)
            )

            if (response.isSuccessful && response.body() != null) {

                val body = response.body()!!

                sessionManager.saveSession(
                    body.accessToken,
                    body.refreshToken,
                    body.idUsuario
                )
                true
            } else {
                sessionManager.clearSession()
                false
            }

        } catch (e: Exception) {
            sessionManager.clearSession()
            false
        }
    }
}