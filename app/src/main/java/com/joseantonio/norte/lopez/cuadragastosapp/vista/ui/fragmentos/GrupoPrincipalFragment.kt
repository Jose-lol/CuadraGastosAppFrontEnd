package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

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

        val btnAddGrupo = view.findViewById<ImageButton>(R.id.btnAddGrupo)

        setupRecyclerView(view)

        sessionManager = SessionManager(requireContext())

        val idUsuario = sessionManager.getIdUsuario()

        if(idUsuario != -1) {
            viewModel.cargarMisGrupos()
        }else{
            Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
        }


        viewModel.usuarioGrupo.observe(viewLifecycleOwner) { listaUsuarioGrupos ->
            listaUsuarioGrupo?.let {
                listaUsuarioGrupo.clear()
                listaUsuarioGrupo.addAll(listaUsuarioGrupos)
                usuarioGrupoAdapter.notifyDataSetChanged()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), "ERROR: $msg", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddGrupo.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_grupo_principal_to_fragmento_crear_grupo)
        }
    }

    private fun setupRecyclerView(view: View) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvGroups)


        usuarioGrupoAdapter = UsuarioGrupoAdapter(listaUsuarioGrupo) { usuarioGrupo ->

            sharedViewModel.seleccionarGrupo(usuarioGrupo)

            findNavController().navigate(R.id.action_fragmento_grupo_principal_to_fragmento_detalle_grupo)
        }

        recyclerView.apply {
            adapter = usuarioGrupoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}