package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "RegistroFragment"
    }

    private val viewModel: GuardarUsuarioViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = AuthRepository(requireContext().applicationContext)
                return GuardarUsuarioViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado y listo")

        val btnRegistro = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnRegistro)
        val btnIrLogin = view.findViewById<TextView>(R.id.btnIrLogin)
        val etEmail = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_registrar_email)
        val etPassword = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_registrar_password)

        btnIrLogin.setOnClickListener {
            Log.i(TAG, "Click en btnIrLogin: Navegando hacia la pantalla de Login")
            findNavController().navigate(R.id.action_fragmento_registro_to_fragmento_login)
        }

        btnRegistro.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            Log.i(TAG, "Click en btnRegistro: Intento de registro iniciado para el email: $email")

            if (email.isEmpty() || password.isEmpty()) {
                Log.w(TAG, "Validación fallida: Campos vacíos. Email o contraseña no introducidos.")
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Campos válidos. Enviando datos al ViewModel...")
            viewModel.registrarUsuario(email, password)
        }

        // Observador del resultado del ViewModel
        viewModel.resultado.observe(viewLifecycleOwner) { mensaje ->
            Log.d(TAG, "Observador de resultado: Recibido mensaje del ViewModel -> '$mensaje'")

            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()

            if (mensaje.startsWith("Éxito")) {
                Log.i(TAG, "Registro exitoso. Volviendo a la pantalla anterior en el backstack.")
                parentFragmentManager.popBackStack()
            } else {
                Log.e(TAG, "Error en el proceso de registro: El resultado no fue exitoso. Mensaje: $mensaje")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando la vista del Fragment de Registro")
    }
}