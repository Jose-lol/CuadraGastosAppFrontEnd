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

        val btnLogin = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)
        val btnRegistro = view.findViewById<TextView>(R.id.btnIrRegistro)
        val emailField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtEmail)
        val passwordField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtPassword)
        val btnGoogleLogin = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoogleLogin)

        btnGoogleLogin.setOnClickListener {
            ejecutarFlujoGoogle()
        }

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.usuario.observe(viewLifecycleOwner) { userResponse ->
            userResponse?.let {

                Toast.makeText(requireContext(), "Bienvenido, ${it.usuario?.email}", Toast.LENGTH_SHORT).show()
                if(it.usuario?.nombre == null || it.usuario.nombre.isEmpty() || it.usuario.telefono ==  null || it.usuario.telefono.isEmpty()){
                    findNavController().navigate(R.id.action_fragmento_login_to_fragmento_completar_perfil)
                }else{
                    findNavController().navigate(R.id.action_fragmento_login_to_fragmento_grupo_principal)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        btnRegistro.setOnClickListener {
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

                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )

                val credential = result.credential

                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    val token = googleIdTokenCredential.idToken
                    viewModel.loginWithGoogle(GoogleLoginRequest(token))

                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("Login", "La credencial recibida no es un ID Token de Google: ${e.message}")
                }

            } catch (e: GetCredentialException) {
                Log.e("GoogleAuth", "Credential error: ${e.message}", e)
            }
        }
    }
}