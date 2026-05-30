package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GastoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.GastoAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.UsuarioGrupoAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.DetalleGruposViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class DetalleGrupoFragment : Fragment(R.layout.fragment_detalle_grupo) {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: DetalleGruposViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = GastoRepository(requireContext().applicationContext)
                return DetalleGruposViewModel(repo) as T
            }
        }
    }

    private var idGrupo: Int? = null
    private lateinit var gastoAdapter: GastoAdapter

    private var listaGastoGrupo = mutableListOf<GastoResponse>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombreGrupo = view.findViewById<TextView>(R.id.tvNombreGrupoDetalle)
        val btnConfiguracion = view.findViewById<ImageButton>(R.id.btnConfiguracion)
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnAnadirAmigo = view.findViewById<MaterialButton>(R.id.btnAnadirAmigo)
        val fabAnadirGasto = view.findViewById<ExtendedFloatingActionButton>(R.id.fabAnadirGasto)

        setupRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.miUsuarioEnGrupo.collect { usuarioGrupo ->
                    usuarioGrupo?.let { miUsuario ->
                        val idDelGrupoValido = miUsuario.grupo.idGrupo

                        if (idDelGrupoValido != null) {
                            tvNombreGrupo.text = miUsuario.grupo.nombre

                            if (idGrupo != idDelGrupoValido) {
                                idGrupo = idDelGrupoValido
                                viewModel.cargarGastosGrupo(idGrupo)
                            }
                        }
                        val soyAdmin = miUsuario.rol == "ADMINISTRADOR"
                        btnAnadirAmigo.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        viewModel.gastoGrupo.observe(viewLifecycleOwner) { listaGastos ->
            listaGastoGrupo.let {
                listaGastoGrupo.clear()
                listaGastoGrupo.addAll(listaGastos)
                gastoAdapter.notifyDataSetChanged()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), "ERROR: $msg", Toast.LENGTH_SHORT).show()
            }
        }

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
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_anadir_gasto)
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvContenidoDetalle)

        gastoAdapter = GastoAdapter(listaGastoGrupo) { gasto ->
            sharedViewModel.seleccionarGasto(gasto)
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_detalle_gasto)
        }

        recyclerView.apply {
            adapter = gastoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}