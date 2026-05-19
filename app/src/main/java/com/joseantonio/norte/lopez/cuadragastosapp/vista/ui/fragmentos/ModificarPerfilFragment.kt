package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

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
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.LoginClienteViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.ModificarPerfilViewModel
import kotlin.getValue

class ModificarPerfilFragment : Fragment(R.layout.fragment_modificar_perfil) {

    private val viewModel: ModificarPerfilViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val context = requireContext().applicationContext

                sessionManager = SessionManager(context)

                val repo = UsuarioRepository(context)

                return ModificarPerfilViewModel(repo,sessionManager) as T
            }
        }
    }

    private lateinit var sessionManager: SessionManager




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefono)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)

        sessionManager = SessionManager(requireContext())
        val idUsuario = sessionManager.getIdUsuario()

        etNombre.setText(sessionManager.getNombre())
        etTelefono.setText(sessionManager.getTelefono())
        etEmail.setText(sessionManager.getEmail())

        if (idUsuario == -1) {
            Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateUsuarioResult.observe(viewLifecycleOwner) { updated ->
            if (updated != null) {
                Toast.makeText(requireContext(), "Cliente actualizado", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_fragmento_modificar_perfil_to_fragmento_cuenta_cliente
                )
            } else {
                Toast.makeText(requireContext(), "No se pudo modificar", Toast.LENGTH_SHORT).show()
            }
        }

        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val email = etEmail.text.toString().trim()

            val usuarioRequest = UsuarioRequest(
                nombre = nombre,
                telefono = telefono,
                email = email,
            )

            viewModel.modificarUsuario(usuarioRequest)
        }
    }
}