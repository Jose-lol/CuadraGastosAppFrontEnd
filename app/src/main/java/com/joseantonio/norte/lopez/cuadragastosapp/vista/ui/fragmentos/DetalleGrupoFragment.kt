package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.DetalleGruposViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class DetalleGrupoFragment : Fragment(R.layout.fragment_detalle_grupo) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

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

        val tvNombreGrupo = view.findViewById<TextView>(R.id.tvNombreGrupoDetalle)
        val btnConfiguracion = view.findViewById<ImageButton>(R.id.btnConfiguracion)
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnAnadirAmigo = view.findViewById<MaterialButton>(R.id.btnAnadirAmigo)
        val fabAnadirGasto = view.findViewById<ExtendedFloatingActionButton>(R.id.fabAnadirGasto)

        // Usamos repeatOnLifecycle para que sea seguro y no gaste recursos en segundo plano
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.miUsuarioEnGrupo.collect { grupo ->
                    grupo?.let {
                        tvNombreGrupo.text = it.grupo.nombre
                    }
                }
            }
        }
        val soyAdmin = sharedViewModel.miUsuarioEnGrupo.value?.rol == "ADMINISTRADOR"
        btnAnadirAmigo.visibility = if (soyAdmin) View.VISIBLE else View.GONE

        btnAnadirAmigo.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_anadir_amigo_a_grupo)
        }

        btnConfiguracion.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_configurar_grupo)
        }

        btnAtras.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_grupo_principal)
        }

        fabAnadirGasto.setOnClickListener {
            // Asumiendo que crearás esta acción en tu nav_graph
            // findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_crear_gasto)
        }
    }

}