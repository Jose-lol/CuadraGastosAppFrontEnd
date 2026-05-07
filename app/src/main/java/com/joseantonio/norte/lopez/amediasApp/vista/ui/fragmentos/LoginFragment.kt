package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.LoginClienteViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {


    private val viewModel: LoginClienteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val context = requireContext().applicationContext

                val sessionManager = SessionManager(context)

                val repo = UsuarioRepository(context)

                return LoginClienteViewModel(repo,sessionManager ) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)
        val btnRegistro = view.findViewById<TextView>(R.id.btnIrRegistro)
        val emailField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtEmail)
        val passwordField = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtPassword)

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

                Toast.makeText(requireContext(), "Bienvenido, ${it.email}", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_fragmento_login_to_fragmento_grupo_principal)
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
}