package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.cuadragastosapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.UsuarioGrupoAdapter
import kotlin.getValue
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.CargarGruposViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel

class GrupoPrincipalFragment : Fragment(R.layout.fragment_grupo_principal) {

    companion object {
        private const val TAG = "GrupoPrincipalFragment"
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var usuarioGrupoAdapter: UsuarioGrupoAdapter
    private var listaUsuarioGrupo = mutableListOf<UsuarioGrupoResponse>()

    private lateinit var sessionManager: SessionManager

    private val viewModel: CargarGruposViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = UsuarioGrupoRepository(requireContext().applicationContext)
                return CargarGruposViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val btnAddGrupo = view.findViewById<ImageButton>(R.id.btnAddGrupo)

        setupRecyclerView(view)

        sessionManager = SessionManager(requireContext())

        val idUsuario = sessionManager.getIdUsuario()
        Log.d(TAG, "Comprobando ID de usuario en sesión: $idUsuario")

        if(idUsuario != -1) {
            Log.d(TAG, "ID válido. Solicitando carga de grupos al viewModel")
            viewModel.cargarMisGrupos()
        }else{
            Log.e(TAG, "Error: idUsuario es -1. No se encontraron preferencias válidas")
            Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
        }

        viewModel.usuarioGrupo.observe(viewLifecycleOwner) { listaUsuarioGrupos ->
            Log.d(TAG, "Observador usuarioGrupo: Recibidos ${listaUsuarioGrupos?.size ?: 0} grupos")
            listaUsuarioGrupo?.let {
                listaUsuarioGrupo.clear()
                listaUsuarioGrupo.addAll(listaUsuarioGrupos)
                usuarioGrupoAdapter.notifyDataSetChanged()
                Log.d(TAG, "Adapter actualizado con los nuevos grupos")
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), "ERROR: $msg", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddGrupo.setOnClickListener {
            Log.i(TAG, "Click en btnAddGrupo: Navegando a crear grupo")
            findNavController().navigate(R.id.action_fragmento_grupo_principal_to_fragmento_crear_grupo)
        }
    }

    private fun setupRecyclerView(view: View) {
        Log.d(TAG, "Inicializando RecyclerView de grupos")
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvGroups)

        usuarioGrupoAdapter = UsuarioGrupoAdapter(listaUsuarioGrupo) { usuarioGrupo ->
            Log.i(TAG, "Click en item del RecyclerView. Grupo seleccionado ID: ${usuarioGrupo.idUsuario}")
            sharedViewModel.seleccionarGrupo(usuarioGrupo)
            findNavController().navigate(R.id.action_fragmento_grupo_principal_to_fragmento_detalle_grupo)
        }

        recyclerView.apply {
            adapter = usuarioGrupoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}