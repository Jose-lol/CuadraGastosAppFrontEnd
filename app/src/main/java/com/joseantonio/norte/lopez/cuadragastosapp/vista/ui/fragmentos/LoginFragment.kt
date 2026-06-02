package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos


import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.LoginClienteViewModel
import android.util.Log
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GoogleLoginRequest

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private lateinit var sessionManager: SessionManager
    private val viewModel: LoginClienteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val context = requireContext().applicationContext

                sessionManager = SessionManager(context)

                val repo = AuthRepository(context)

                return LoginClienteViewModel(repo,sessionManager) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val btnLogin = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)
        val btnRegistro = view.findViewById<TextView>(R.id.btnIrRegistro)
        val emailField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtEmail)
        val passwordField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtPassword)
        val btnGoogleLogin = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoogleLogin)

        btnGoogleLogin.setOnClickListener {
            Log.i(TAG, "Click en btnGoogleLogin: Iniciando flujo de Google")
            ejecutarFlujoGoogle()
        }

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            Log.i(TAG, "Click en btnLogin: Intento de inicio de sesión para el email: $email")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(TAG, "Campos no vacíos. Llamando a viewModel.login")
                viewModel.login(email, password)
            } else {
                Log.w(TAG, "Validación fallida: Email o contraseña vacíos")
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.usuario.observe(viewLifecycleOwner) { userResponse ->
            Log.d(TAG, "Observador usuario: Resultado recibido = $userResponse")
            userResponse?.let {
                if(it.usuario?.nombre == null || it.usuario.nombre.isEmpty() || it.usuario.telefono ==  null || it.usuario.telefono.isEmpty()){
                    Log.i(TAG, "Perfil incompleto. Navegando a completar perfil")
                    findNavController().navigate(R.id.action_fragmento_login_to_fragmento_completar_perfil)
                }else{
                    Log.i(TAG, "Perfil completo. Navegando al grupo principal")
                    findNavController().navigate(R.id.action_fragmento_login_to_fragmento_grupo_principal)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Recibido mensaje de error -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        btnRegistro.setOnClickListener {
            Log.i(TAG, "Click en btnRegistro: Navegando hacia la pantalla de Registro")
            findNavController().navigate(R.id.action_fragmento_login_to_fragmento_registro)
        }
    }

    private fun ejecutarFlujoGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.google_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val credentialManager = CredentialManager.create(requireContext())
                Log.d(TAG, "Llamando a credentialManager.getCredential")

                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )

                val credential = result.credential

                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val token = googleIdTokenCredential.idToken
                    Log.d(TAG, "Token obtenido con éxito. Enviando a viewModel.loginWithGoogle")
                    viewModel.loginWithGoogle(GoogleLoginRequest(token))

                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "La credencial recibida no es un ID Token de Google: ${e.message}")
                }

            } catch (e: GetCredentialException) {
                Log.e(TAG, "Credential error: ${e.message}", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}