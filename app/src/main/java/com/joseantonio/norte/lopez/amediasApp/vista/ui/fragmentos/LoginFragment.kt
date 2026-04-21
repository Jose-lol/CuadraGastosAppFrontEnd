package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.session.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.LoginClienteViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginClienteViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Pasamos el ApplicationContext para mayor seguridad
                val repo = UsuarioRepository(requireContext().applicationContext)
                return LoginClienteViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a las vistas
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val emailField = view.findViewById<EditText>(R.id.txtEmail)
        val passwordField = view.findViewById<EditText>(R.id.txtPassword)
        val btnIrRegistro = view.findViewById<Button>(R.id.btnIrRegistro)

        // Listener del botón de login
        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            user?.let {
                Toast.makeText(requireContext(), "Hola ${it.nombre}", Toast.LENGTH_SHORT).show()
                SessionManager.usuario = it
                findNavController().navigate(R.id.action_fragmento_login_to_fragmento_grupo_principal)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), "Credenciales incorrectas o error de conexión", Toast.LENGTH_SHORT).show()
            }
        }

        btnIrRegistro.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_login_to_fragmento_registro)
        }
    }
}
