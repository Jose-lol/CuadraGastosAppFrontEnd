package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.CompletarPerfilViewModel

class CompletarPerfilFragment : Fragment(R.layout.fragment_completar_perfil) {

    private val viewModel: CompletarPerfilViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Cambia el repositorio por el que maneje la tabla Usuarios
                val repo = UsuarioRepository(requireContext().applicationContext)
                return CompletarPerfilViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombre)
        val etTelefono = view.findViewById<TextInputEditText>(R.id.etTelefono)
        val btnFinalizar = view.findViewById<Button>(R.id.btnFinalizarRegistro)

        btnFinalizar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val telefonoBruto = etTelefono.text.toString().trim()

            // Validaciones básicas antes de enviar
            if (nombre.isEmpty()) {
                etNombre.error = "Introduce tu nombre"
                return@setOnClickListener
            }

            if (telefonoBruto.length < 9) {
                etTelefono.error = "Introduce un teléfono válido"
                return@setOnClickListener
            }

            val telefonoLimpio = limpiarTelefono(telefonoBruto)

            viewModel.actualizarPerfil(nombre, telefonoLimpio)
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "¡Perfil completado!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_fragmento_completar_perfil_to_fragmento_grupo_principal)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpiarTelefono(tel: String?): String {
        return tel?.replace(Regex("[^0-9]"), "")?.takeLast(9) ?: ""
    }
}