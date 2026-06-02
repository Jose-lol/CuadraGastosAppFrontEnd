package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "CompletarPerfilFragment"
    }

    private val viewModel: CompletarPerfilViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = UsuarioRepository(requireContext().applicationContext)
                return CompletarPerfilViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombre)
        val etTelefono = view.findViewById<TextInputEditText>(R.id.etTelefono)
        val btnFinalizar = view.findViewById<Button>(R.id.btnFinalizarRegistro)

        btnFinalizar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val telefonoBruto = etTelefono.text.toString().trim()

            Log.i(TAG, "Click en btnFinalizar: Intento de completar perfil. Nombre='$nombre', TelefonoBruto='$telefonoBruto'")

            if (nombre.isEmpty()) {
                Log.w(TAG, "Validación fallida: El campo nombre está vacío")
                etNombre.error = "Introduce tu nombre"
                return@setOnClickListener
            }

            if (telefonoBruto.length < 9) {
                Log.w(TAG, "Validación fallida: El teléfono tiene una longitud inferior a 9 caracteres")
                etTelefono.error = "Introduce un teléfono válido"
                return@setOnClickListener
            }

            val telefonoLimpio = limpiarTelefono(telefonoBruto)
            Log.d(TAG, "Teléfono formateado correctamente: '$telefonoLimpio'. Enviando al viewModel")

            viewModel.actualizarPerfil(nombre, telefonoLimpio)
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) {
            Log.i(TAG, "Observador registroExitoso: Perfil guardado con éxito. Navegando al grupo principal")
            Toast.makeText(requireContext(), "¡Perfil completado!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_fragmento_completar_perfil_to_fragmento_grupo_principal)
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpiarTelefono(tel: String?): String {
        val limpio = tel?.replace(Regex("[^0-9]"), "")?.takeLast(9) ?: ""
        Log.d(TAG, "limpiarTelefono: Procesado '$tel' -> Resultado: '$limpio'")
        return limpio
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}