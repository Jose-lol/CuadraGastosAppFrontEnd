package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.GuardarUsuarioViewModel
import kotlin.getValue

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    // 1. Usa la delegación 'by viewModels'
    private val viewModel: GuardarUsuarioViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Pasamos el ApplicationContext para mayor seguridad
                val repo = AuthRepository(requireContext().applicationContext)
                return GuardarUsuarioViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegistro = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnRegistro)
        val btnIrLogin = view.findViewById<TextView>(R.id.btnIrLogin)
        val etEmail = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_registrar_email)
        val etPassword = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_registrar_password)

        // Navegar a login
        btnIrLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_registro_to_fragmento_login)
        }

        // Registro de cliente
        btnRegistro.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.registrarUsuario(email, password)
        }

        // Observamos el LiveData del resultado
        viewModel.resultado.observe(viewLifecycleOwner) { mensaje ->

            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()

            if (mensaje.startsWith("Éxito")) {
                parentFragmentManager.popBackStack()
            }
        }
    }
}