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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Grupo
import com.joseantonio.norte.lopez.amediasApp.session.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.factory.ConfigurarGruposViewModelFactory
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.ConfigurarGruposViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.GrupoSharedViewModel
import kotlinx.coroutines.launch
import kotlin.getValue


class ConfigurarGrupoFragment : Fragment(R.layout.fragment_configurar_grupo) {

    private val sharedViewModel: GrupoSharedViewModel by activityViewModels()
    private val viewModel: ConfigurarGruposViewModel by viewModels {
        ConfigurarGruposViewModelFactory(
            GrupoRepository(requireContext().applicationContext),
            UsuarioGrupoRepository(requireContext().applicationContext)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnEliminarGrupo = view.findViewById<Button>(R.id.btnEliminarGrupo)
        val btnSalirGrupo = view.findViewById<Button>(R.id.btnSalirGrupo)

        btnEliminarGrupo.setOnClickListener {

            val idGrupo = sharedViewModel.grupoSeleccionado.value?.idGrupo

            viewModel.eliminarGrupo(idGrupo)
        }

        btnSalirGrupo.setOnClickListener {

            val idGrupo = sharedViewModel.grupoSeleccionado.value?.idGrupo
            val idUsuario = SessionManager.usuario.idUsuario

            viewModel.desactivarUsuarioGrupo(idGrupo,idUsuario)
        }

        viewModel.grupo.observe(viewLifecycleOwner) { grupos ->
            grupos?.let {
                Toast.makeText(
                    requireContext(),
                    "grupo eliminado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
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