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

    companion object {
        private const val TAG = "DetalleGrupoFragment"
    }

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
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val tvNombreGrupo = view.findViewById<TextView>(R.id.tvNombreGrupoDetalle)
        val btnConfiguracion = view.findViewById<ImageButton>(R.id.btnConfiguracion)
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnAnadirAmigo = view.findViewById<MaterialButton>(R.id.btnAnadirAmigo)
        val fabAnadirGasto = view.findViewById<ExtendedFloatingActionButton>(R.id.fabAnadirGasto)

        setupRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.miUsuarioEnGrupo.collect { usuarioGrupo ->
                    Log.d(TAG, "StateFlow miUsuarioEnGrupo emitió: $usuarioGrupo")
                    usuarioGrupo?.let { miUsuario ->
                        val idDelGrupoValido = miUsuario.grupo.idGrupo

                        if (idDelGrupoValido != null) {
                            tvNombreGrupo.text = miUsuario.grupo.nombre

                            if (idGrupo != idDelGrupoValido) {
                                Log.d(TAG, "Detectado nuevo idGrupo: $idDelGrupoValido. Cargando gastos...")
                                idGrupo = idDelGrupoValido
                                viewModel.cargarGastosGrupo(idGrupo)
                            }
                        }
                        val soyAdmin = miUsuario.rol == "ADMINISTRADOR"
                        Log.d(TAG, "Rol de usuario: ${miUsuario.rol} (soyAdmin = $soyAdmin)")
                        btnAnadirAmigo.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        viewModel.gastoGrupo.observe(viewLifecycleOwner) { listaGastos ->
            Log.d(TAG, "Observador gastoGrupo: Recibidos ${listaGastos?.size ?: 0} gastos")
            listaGastoGrupo.let {
                listaGastoGrupo.clear()
                listaGastoGrupo.addAll(listaGastos)
                gastoAdapter.notifyDataSetChanged()
                Log.d(TAG, "Adapter actualizado con los nuevos gastos")
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), "ERROR: $msg", Toast.LENGTH_SHORT).show()
            }
        }

        btnAnadirAmigo.setOnClickListener {
            Log.i(TAG, "Click en btnAnadirAmigo: Navegando a añadir amigo al grupo")
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_anadir_amigo_a_grupo)
        }

        btnConfiguracion.setOnClickListener {
            Log.i(TAG, "Click en btnConfiguracion: Navegando a configurar grupo")
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_configurar_grupo)
        }

        btnAtras.setOnClickListener {
            Log.i(TAG, "Click en btnAtras: Volviendo al grupo principal")
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_grupo_principal)
        }

        fabAnadirGasto.setOnClickListener {
            Log.i(TAG, "Click en fabAnadirGasto: Navegando a añadir gasto")
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_anadir_gasto)
        }
    }

    private fun setupRecyclerView(view: View) {
        Log.d(TAG, "Inicializando RecyclerView de gastos")
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvContenidoDetalle)

        gastoAdapter = GastoAdapter(listaGastoGrupo) { gasto ->
            Log.i(TAG, "Click en item del RecyclerView. Gasto seleccionado ID: ${gasto.idGasto}")
            sharedViewModel.seleccionarGasto(gasto)
            findNavController().navigate(R.id.action_fragmento_detalle_grupo_to_fragmento_detalle_gasto)
        }

        recyclerView.apply {
            adapter = gastoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}