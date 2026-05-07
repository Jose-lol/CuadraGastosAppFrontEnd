package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.Repository.UsuarioRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.local.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.ui.adapter.AmigosAdapter
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.AnadirAmigoAGrupoViewModel
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.SharedViewModel
import kotlin.getValue


class AnadirAmigoAGrupoFragment : Fragment(R.layout.fragment_anadir_amigo_a_grupo) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var grupoRequest: GrupoRequest

    lateinit var sessionManager: SessionManager

    private lateinit var amigosAdapter: AmigosAdapter

    private val viewModel: AnadirAmigoAGrupoViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo1 = UsuarioGrupoRepository(requireContext().applicationContext)
                val repo2 = UsuarioRepository(requireContext().applicationContext)
                return AnadirAmigoAGrupoViewModel(repo1,repo2) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnFinalizar= view.findViewById<MaterialButton>(R.id.btnFinalizar)
        var searchViewAmigos = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchViewAmigos)
        val rvMisAmigos = view.findViewById<RecyclerView>(R.id.rvMisAmigos)


        amigosAdapter = AmigosAdapter { cantidad ->
            if (cantidad > 0) {
                btnFinalizar.isEnabled = true
                btnFinalizar.text = "Confirmar $cantidad miembros"
            } else {
                btnFinalizar.isEnabled = false
                btnFinalizar.text = "Confirmar miembros"
            }
        }

        sessionManager = SessionManager(requireContext())
        grupoRequest = GrupoRequest()

        val idUsuario = sessionManager.getIdUsuario()

        rvMisAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvMisAmigos.adapter = amigosAdapter
        if(idUsuario != -1 ){
             viewModel.cargarAmigos(idUsuario)
        }else{
            Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
        }

        viewModel.listaAmigos.observe(viewLifecycleOwner) { amigos ->
            amigosAdapter.actualizarLista(amigos.toList())
        }

        btnFinalizar.setOnClickListener {

            grupoRequest.idGrupo = sharedViewModel.grupoSeleccionado.value?.idGrupo
            val seleccionados = amigosAdapter.getIdsSeleccionados()

            if (seleccionados.isNotEmpty()) {

                val listaMiembros = seleccionados.map { id ->
                    UsuarioResponse(idUsuario = id)
                }
                grupoRequest.miembros = listaMiembros

                viewModel.anadirAmigoAGrupo(grupoRequest)

                Toast.makeText(requireContext(), "Añadiendo ${seleccionados.size} amigos", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }

        viewModel.resultado.observe(viewLifecycleOwner) { resultado ->

                Toast.makeText(
                    requireContext(),
                    "Usuario añadido al grupo correctamente",
                    Toast.LENGTH_SHORT
                ).show()

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
            findNavController().navigate(R.id.action_fragmento_anadir_amigo_a_grupo_to_fragmento_detalle_grupo)
        }
    }
}