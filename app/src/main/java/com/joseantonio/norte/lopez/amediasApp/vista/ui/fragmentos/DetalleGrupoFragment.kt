package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.DetalleGruposViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.GrupoSharedViewModel
import kotlin.getValue

class DetalleGrupoFragment : Fragment(R.layout.fragment_detalle_grupo) {

    private val sharedViewModel: GrupoSharedViewModel by activityViewModels()

    private val viewModel: DetalleGruposViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = GrupoRepository(requireContext().applicationContext)
                return DetalleGruposViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val btnConfiguracion = view.findViewById<ImageButton>(R.id.btnConfiguracion)

            sharedViewModel.grupoSeleccionado

            btnConfiguracion.setOnClickListener {
                findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_configurar_grupo)
            }

    }

}