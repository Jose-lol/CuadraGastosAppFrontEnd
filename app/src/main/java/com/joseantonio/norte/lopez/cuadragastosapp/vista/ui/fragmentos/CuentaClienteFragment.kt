package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "CuentaClienteFragment"
    }

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
        Log.d(TAG, "onViewCreated: Fragment cargado")

        sessionManager = SessionManager(requireContext())
        val tvNombreCuenta = view.findViewById<TextView>(R.id.tvNombreCuenta)
        val tvEmailCuenta = view.findViewById<TextView>(R.id.tvEmailCuenta)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditarPerfil = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnEditarPerfil)

        val nombreUsuario = sessionManager.getNombre()
        val emailUsuario = sessionManager.getEmail()
        Log.d(TAG, "Cargando datos de perfil. Nombre: $nombreUsuario, Email: $emailUsuario")

        if (nombreUsuario != null && emailUsuario != null) {
            tvNombreCuenta.text = nombreUsuario
            tvEmailCuenta.text = emailUsuario
        } else {
            Log.w(TAG, "Advertencia: Datos de usuario incompletos en SessionManager")
        }

        btnEditarPerfil.setOnClickListener {
            Log.i(TAG, "Click en btnEditarPerfil: Navegando a modificar perfil")
            findNavController().navigate(R.id.action_fragmento_cuenta_cliente_to_fragmento_modificar_perfil)
        }

        btnLogout.setOnClickListener {
            Log.i(TAG, "Click en btnLogout: Iniciando proceso de cierre de sesión")
            viewModel.logout(sessionManager.getAccessToken())
            findNavController().navigate(R.id.action_fragmento_cuenta_cliente_to_fragmento_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}