package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.ConfigurarGruposViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.SharedViewModel
import kotlin.getValue


class ConfigurarGrupoFragment : Fragment(R.layout.fragment_configurar_grupo) {

    lateinit var grupoRequest : GrupoRequest

    lateinit var sessionManager: SessionManager

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ConfigurarGruposViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = UsuarioGrupoRepository(requireContext().applicationContext)
                return ConfigurarGruposViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnSalirGrupo = view.findViewById<Button>(R.id.btnSalirGrupo)

        grupoRequest = GrupoRequest()

        btnSalirGrupo.setOnClickListener {

            sessionManager = SessionManager(requireContext())
            val idUsuario = sessionManager.getIdUsuario()

            if(idUsuario != -1 ){
                grupoRequest.idUsuario = idUsuario
                val idGrupo = sharedViewModel.grupoSeleccionado.value?.idGrupo
                grupoRequest.idGrupo=idGrupo
                viewModel.desactivarUsuarioGrupo(grupoRequest)
            }else{
                Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
            }


        }

        viewModel.grupo.observe(viewLifecycleOwner) { grupos ->
            grupos?.let {
                Toast.makeText(
                    requireContext(),
                    "grupo eliminado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_grupo_principal)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnAtras.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_detalle_grupo)
        }
    }
}