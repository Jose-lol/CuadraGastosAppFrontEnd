package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.UsuarioGrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.UsuarioGrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.MiembrosConfigAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.ConfigurarGruposViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlin.getValue


class ConfigurarGrupoFragment : Fragment(R.layout.fragment_configurar_grupo) {

    lateinit var sessionManager: SessionManager
    private lateinit var miembrosAdapter: MiembrosConfigAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ConfigurarGruposViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = UsuarioGrupoRepository(requireContext().applicationContext)
                return ConfigurarGruposViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val miIdUsuario = sessionManager.getIdUsuario()

        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtras)
        val btnSalirGrupo = view.findViewById<Button>(R.id.btnSalirGrupo)
        val tvModoAdmin = view.findViewById<TextView>(R.id.tvModoAdmin)
        val rvMiembrosConfig = view.findViewById<RecyclerView>(R.id.rvMiembrosConfig)
        val btnActualizarNombre = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnActualizarNombre)
        val usuarioGrupo = sharedViewModel.miUsuarioEnGrupo.value
        val idGrupoActual = usuarioGrupo?.grupo?.idGrupo
        val etNombreGrupo = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etNombreGrupo)
        val tvNombreGrupoActual = view.findViewById<TextView>(R.id.tvNombreGrupoActual)
        val tilNombreGrupo = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilNombreGrupo)
        val soyAdmin = usuarioGrupo?.rol == "ADMINISTRADOR"

        tvNombreGrupoActual.text = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.nombre

        miembrosAdapter = MiembrosConfigAdapter(
            miId = miIdUsuario,
            onEliminarClick = { miembroId, nombre ->
                mostrarDialogoEliminar(miembroId, nombre, idGrupoActual)
            },
            onHacerAdminClick = { miembroId, nombre ->
                mostrarDialogoHacerAdmin(miembroId, nombre, idGrupoActual)
            }
        )
        rvMiembrosConfig.layoutManager = LinearLayoutManager(requireContext())
        rvMiembrosConfig.adapter = miembrosAdapter

        if (idGrupoActual != null) {
            viewModel.cargarMiembrosGrupo(idGrupoActual)
        }
        btnActualizarNombre.setOnClickListener {
            viewModel.cambiarNombreGrupo(GrupoRequest(
                idGrupo =   idGrupoActual,
                nombre = etNombreGrupo.text.toString()
            ))
        }

        btnSalirGrupo.setOnClickListener {
            mostrarDialogoSalirGrupo(miIdUsuario ?: -1,idGrupoActual)
        }

        btnAtras.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_detalle_grupo)
        }

        viewModel.listaMiembros.observe(viewLifecycleOwner) { miembros ->
            miembros?.let {

                tvModoAdmin.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                btnActualizarNombre.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                tilNombreGrupo.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                miembrosAdapter.setSoyAdmin(soyAdmin)
                miembrosAdapter.submitList(it)
            }
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Operación realizada con éxito", Toast.LENGTH_SHORT).show()
        }

        viewModel.usuarioGrupo.observe(viewLifecycleOwner) {usuarioGrupo->
            if(soyAdmin){
                sharedViewModel.seleccionarGrupo(usuarioGrupo)
                tvNombreGrupoActual.text = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.nombre
                Toast.makeText(requireContext(), "Operación realizada con éxito", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Debes ser administrador para cambiar el nombre", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoSalirGrupo(idUsuario: Int, idGrupo: Int?) {
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Salir del grupo")
            .setMessage("¿Estás seguro de que quieres salir del grupo?")
            .setPositiveButton("Salir") { _, _ ->
                if (idUsuario != -1) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.desactivarUsuarioGrupo(usuarioGrupoRequest)
                    findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_grupo_principal)
                    sharedViewModel.seleccionarGrupo(
                        UsuarioGrupoResponse(GrupoResponse(null),-1,"","","")
                    )
                } else {
                    Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(idUsuario: Int, nombre: String, idGrupo: Int?) {
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Expulsar miembro")
            .setMessage("¿Estás seguro de que quieres eliminar a $nombre de este grupo?")
            .setPositiveButton("Eliminar") { _, _ ->
                if (idGrupo != null) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.desactivarUsuarioGrupo(usuarioGrupoRequest)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoHacerAdmin(idUsuario: Int, nombre: String, idGrupo: Int?) {
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Dar permisos de Administrador")
            .setMessage("¿Quieres hacer a $nombre administrador? Tendrá permisos para modificar el grupo y expulsar a otros miembros.")
            .setPositiveButton("Confirmar") { _, _ ->
                if (idGrupo != null) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.hacerAdministrador( usuarioGrupoRequest)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}