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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ContactoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.AmigosAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.AnadirAmigoAGrupoViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlin.getValue


class AnadirAmigoAGrupoFragment : Fragment(R.layout.fragment_anadir_amigo_a_grupo) {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: AnadirAmigoAGrupoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo1 = UsuarioGrupoRepository(requireContext().applicationContext)
                val repo2 = ContactoRepository(requireContext().applicationContext)
                return AnadirAmigoAGrupoViewModel(repo1, repo2) as T
            }
        }
    }

    private lateinit var amigosAdapter: AmigosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnFinalizar = view.findViewById<MaterialButton>(R.id.btnFinalizar)
        val searchViewAmigos = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchViewAmigos)
        val rvMisAmigos = view.findViewById<RecyclerView>(R.id.rvMisAmigos)

        amigosAdapter = AmigosAdapter { cantidad ->
            if (cantidad > 0) {
                btnFinalizar.isChecked = true
                btnFinalizar.text = "Confirmar $cantidad miembros"
            } else {
                btnFinalizar.isChecked = false
                btnFinalizar.text = "Confirmar miembros"
            }
        }

        val idGrupoActual = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.idGrupo

        rvMisAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvMisAmigos.adapter = amigosAdapter

        viewModel.cargarContactosNoEnGrupo(idGrupoActual)

        viewModel.listaAmigos.observe(viewLifecycleOwner) { amigos ->
            amigosAdapter.actualizarLista(amigos.toList())
        }

        searchViewAmigos.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        btnFinalizar.setOnClickListener {
            val idGrupoActual = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.idGrupo
            val idsSeleccionados = amigosAdapter.getIdsSeleccionados()

            if (idGrupoActual != null && idsSeleccionados.isNotEmpty()) {
                idsSeleccionados.forEach { idUsuario ->
                    val request =
                        UsuarioGrupoRequest(idGrupo = idGrupoActual, idUsuario = idUsuario)
                    viewModel.anadirAmigoAGrupo(request)
                }

                Toast.makeText(requireContext(), "Añadiendo miembros...", Toast.LENGTH_SHORT).show()
                amigosAdapter.limpiarSeleccionados()
                btnFinalizar.isEnabled = false
                btnFinalizar.text = "Confirmar miembros"
            }
        }

        viewModel.resultado.observe(viewLifecycleOwner) {msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }

        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }
        btnAtras.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}