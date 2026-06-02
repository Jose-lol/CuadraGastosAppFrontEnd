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

    companion object {
        private const val TAG = "AnadirAmigoAGrupoFrag"
    }

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
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnFinalizar = view.findViewById<MaterialButton>(R.id.btnFinalizar)
        val searchViewAmigos = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchViewAmigos)
        val rvMisAmigos = view.findViewById<RecyclerView>(R.id.rvMisAmigos)

        amigosAdapter = AmigosAdapter { cantidad ->
            Log.d(TAG, "Callback adapter: Cambió selección de miembros. Cantidad = $cantidad")
            if (cantidad > 0) {
                btnFinalizar.isChecked = true
                btnFinalizar.text = "Confirmar $cantidad miembros"
            } else {
                btnFinalizar.isChecked = false
                btnFinalizar.text = "Confirmar miembros"
            }
        }

        val idGrupoActual = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.idGrupo
        Log.d(TAG, "idGrupoActual obtenido desde el SharedViewModel: $idGrupoActual")

        rvMisAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvMisAmigos.adapter = amigosAdapter

        Log.d(TAG, "Llamando a viewModel.cargarContactosNoEnGrupo para el grupo ID: $idGrupoActual")
        viewModel.cargarContactosNoEnGrupo(idGrupoActual)

        viewModel.listaAmigos.observe(viewLifecycleOwner) { amigos ->
            Log.d(TAG, "Observador listaAmigos: Recibidos ${amigos?.size ?: 0} amigos disponibles")
            amigosAdapter.actualizarLista(amigos.toList())
        }

        searchViewAmigos.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "searchViewAmigos texto cambiado: '$newText'")
                return true
            }
        })

        btnFinalizar.setOnClickListener {
            val currentGroupId = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.idGrupo
            val idsSeleccionados = amigosAdapter.getIdsSeleccionados()

            Log.i(TAG, "Click en btnFinalizar: Intentando añadir ${idsSeleccionados.size} miembros al grupo ID: $currentGroupId")

            if (currentGroupId != null && idsSeleccionados.isNotEmpty()) {
                idsSeleccionados.forEach { idUsuario ->
                    Log.d(TAG, "Enviando solicitud para añadir usuario ID: $idUsuario al grupo ID: $currentGroupId")
                    val request = UsuarioGrupoRequest(idGrupo = currentGroupId, idUsuario = idUsuario)
                    viewModel.anadirAmigoAGrupo(request)
                }

                Toast.makeText(requireContext(), "Añadiendo miembros...", Toast.LENGTH_SHORT).show()
                amigosAdapter.limpiarSeleccionados()
                btnFinalizar.isEnabled = false
                btnFinalizar.text = "Confirmar miembros"
            } else {
                Log.w(TAG, "Intento de finalización fallido: currentGroupId es nulo o no hay ids seleccionados")
            }
        }

        viewModel.resultado.observe(viewLifecycleOwner) { msg ->
            Log.d(TAG, "Observador resultado: Recibido mensaje de éxito -> '$msg'")
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
        }

        btnAtras.setOnClickListener {
            Log.i(TAG, "Click en btnAtras: Volviendo atrás en el backstack")
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}