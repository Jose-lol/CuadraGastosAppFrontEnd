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
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.GuardarUsuarioViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.LoginClienteViewModel
import kotlin.getValue

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    // 1. Usa la delegación 'by viewModels'
    private val viewModel: GuardarUsuarioViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Pasamos el ApplicationContext para mayor seguridad
                val repo = UsuarioRepository(requireContext().applicationContext)
                return GuardarUsuarioViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Referencias a la vista
        val btnRegistro = view.findViewById<Button>(R.id.btnRegistro)
        val btnIrLogin = view.findViewById<Button>(R.id.btnIrLogin)
        val etEmail = view.findViewById<EditText>(R.id.et_registrar_email)
        val etPassword = view.findViewById<EditText>(R.id.et_registrar_password)

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

            // Si el mensaje empieza con "Éxito", navegamos hacia atrás o al login
            if (mensaje.startsWith("Éxito")) {
                parentFragmentManager.popBackStack()
            }
        }
    }
}