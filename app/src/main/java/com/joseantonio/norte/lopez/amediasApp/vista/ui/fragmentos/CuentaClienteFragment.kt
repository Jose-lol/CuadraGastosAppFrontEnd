package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.CuentaClienteViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.LoginClienteViewModel
import kotlin.getValue

class CuentaClienteFragment : Fragment(R.layout.fragment_cuenta_cliente) {

    private lateinit var sessionManager: SessionManager

    private val viewModel: CuentaClienteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val context = requireContext().applicationContext
                val sessionManager = SessionManager(context)
                val repo = UsuarioRepository(context)
                return CuentaClienteViewModel(repo,sessionManager ) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val tvNombre = view.findViewById<TextView>(R.id.tvNombreUsuario)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmailUsuario)

        val btnModificar = view.findViewById<Button>(R.id.btnModificarPerfil)
        val btnAjustes = view.findViewById<Button>(R.id.btnAjustes)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // ir a modificar perfil
        btnModificar.setOnClickListener {
            findNavController().navigate(R.id.fragmento_modificar_perfil)
        }

        // ⚙️ ajustes (placeholder)
        btnAjustes.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        // 🚪 logout PRO
        btnLogout.setOnClickListener {
            viewModel.logout(sessionManager.getRefreshToken())
            findNavController().navigate(R.id.action_fragmento_cuenta_cliente_to_fragmento_login)
        }
    }
}