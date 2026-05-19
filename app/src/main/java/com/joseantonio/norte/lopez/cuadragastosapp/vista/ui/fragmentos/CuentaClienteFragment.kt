package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.AuthRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.CuentaClienteViewModel
import kotlin.getValue

class CuentaClienteFragment : Fragment(R.layout.fragment_cuenta_cliente) {

    private lateinit var sessionManager: SessionManager

    private val viewModel: CuentaClienteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val context = requireContext().applicationContext
                val sessionManager = SessionManager(context)
                val repo = AuthRepository(context)
                return CuentaClienteViewModel(repo,sessionManager ) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val tvNombreCuenta = view.findViewById<TextView>(R.id.tvNombreCuenta)
        val tvEmailCuenta = view.findViewById<TextView>(R.id.tvEmailCuenta)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditarPerfil = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnEditarPerfil)
        val nombreUsuario = sessionManager.getNombre()
        val emailUsuario = sessionManager.getEmail()

        if (nombreUsuario != null && emailUsuario != null) {
            tvNombreCuenta.text = nombreUsuario
            tvEmailCuenta.text = emailUsuario
        }

        btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_cuenta_cliente_to_fragmento_modificar_perfil)
        }

        // 🚪 logout PRO
        btnLogout.setOnClickListener {
            viewModel.logout(sessionManager.getRefreshToken())
            findNavController().navigate(R.id.action_fragmento_cuenta_cliente_to_fragmento_login)
        }
    }
}