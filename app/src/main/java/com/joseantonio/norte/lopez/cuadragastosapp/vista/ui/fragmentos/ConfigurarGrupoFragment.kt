package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "ConfigurarGrupoFragment"
    }

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
        Log.d(TAG, "onViewCreated: Fragment cargado")

        sessionManager = SessionManager(requireContext())
        val miIdUsuario = sessionManager.getIdUsuario()
        Log.d(TAG, "ID de usuario actual en sesión: $miIdUsuario")

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

        Log.d(TAG, "Datos iniciales de grupo: idGrupoActual=$idGrupoActual, soyAdmin=$soyAdmin")
        tvNombreGrupoActual.text = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.nombre

        miembrosAdapter = MiembrosConfigAdapter(
            miId = miIdUsuario,
            onEliminarClick = { miembroId, nombre ->
                Log.i(TAG, "Click en eliminar miembro. ID: $miembroId, Nombre: $nombre")
                mostrarDialogoEliminar(miembroId, nombre, idGrupoActual)
            },
            onHacerAdminClick = { miembroId, nombre ->
                Log.i(TAG, "Click en hacer administrador. ID: $miembroId, Nombre: $nombre")
                mostrarDialogoHacerAdmin(miembroId, nombre, idGrupoActual)
            }
        )
        rvMiembrosConfig.layoutManager = LinearLayoutManager(requireContext())
        rvMiembrosConfig.adapter = miembrosAdapter

        if (idGrupoActual != null) {
            Log.d(TAG, "Llamando a viewModel.cargarMiembrosGrupo para el grupo ID: $idGrupoActual")
            viewModel.cargarMiembrosGrupo(idGrupoActual)
        }

        btnActualizarNombre.setOnClickListener {
            val nuevoNombre = etNombreGrupo.text.toString()
            Log.i(TAG, "Click en btnActualizarNombre: Solicitando cambio de nombre a '$nuevoNombre'")
            viewModel.cambiarNombreGrupo(GrupoRequest(
                idGrupo = idGrupoActual,
                nombre = nuevoNombre
            ))
        }

        btnSalirGrupo.setOnClickListener {
            Log.i(TAG, "Click en btnSalirGrupo: Abriendo diálogo de confirmación")
            mostrarDialogoSalirGrupo(miIdUsuario ?: -1, idGrupoActual)
        }

        btnAtras.setOnClickListener {
            Log.i(TAG, "Click en btnAtras: Volviendo a detalle de grupo")
            findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_detalle_grupo)
        }

        viewModel.listaMiembros.observe(viewLifecycleOwner) { miembros ->
            Log.d(TAG, "Observador listaMiembros: Recibidos ${miembros?.size ?: 0} miembros")
            miembros?.let {
                tvModoAdmin.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                btnActualizarNombre.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                tilNombreGrupo.visibility = if (soyAdmin) View.VISIBLE else View.GONE
                miembrosAdapter.setSoyAdmin(soyAdmin)
                miembrosAdapter.submitList(it)
            }
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) {
            Log.d(TAG, "Observador registroExitoso: Operación confirmada")
            Toast.makeText(requireContext(), "Operación realizada con éxito", Toast.LENGTH_SHORT).show()
        }

        viewModel.usuarioGrupo.observe(viewLifecycleOwner) { usuarioGrupo ->
            Log.d(TAG, "Observador usuarioGrupo: Actualización de datos recibida. soyAdmin=$soyAdmin")
            if(soyAdmin){
                sharedViewModel.seleccionarGrupo(usuarioGrupo)
                tvNombreGrupoActual.text = sharedViewModel.miUsuarioEnGrupo.value?.grupo?.nombre
                Toast.makeText(requireContext(), "Operación realizada con éxito", Toast.LENGTH_SHORT).show()
            }else{
                Log.w(TAG, "Intento fallido de actualizar nombre sin rol de administrador")
                Toast.makeText(requireContext(), "Debes ser administrador para cambiar el nombre", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoSalirGrupo(idUsuario: Int, idGrupo: Int?) {
        Log.d(TAG, "Mostrando diálogo: Salir del grupo")
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Salir del grupo")
            .setMessage("¿Estás seguro de que quieres salir del grupo?")
            .setPositiveButton("Salir") { _, _ ->
                Log.i(TAG, "Confirmado: Salir del grupo. idUsuario=$idUsuario, idGrupo=$idGrupo")
                if (idUsuario != -1) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.desactivarUsuarioGrupo(usuarioGrupoRequest)
                    findNavController().navigate(R.id.action_fragmento_configurar_grupo_to_fragmento_grupo_principal)
                    sharedViewModel.seleccionarGrupo(
                        UsuarioGrupoResponse(GrupoResponse(null),-1,"","","")
                    )
                } else {
                    Log.e(TAG, "Error al salir del grupo: Usuario no encontrado en preferencias")
                    Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(idUsuario: Int, nombre: String, idGrupo: Int?) {
        Log.d(TAG, "Mostrando diálogo: Expulsar miembro ($nombre)")
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Expulsar miembro")
            .setMessage("¿Estás seguro de que quieres eliminar a $nombre de este grupo?")
            .setPositiveButton("Eliminar") { _, _ ->
                Log.i(TAG, "Confirmado: Expulsar miembro. idUsuario=$idUsuario, idGrupo=$idGrupo")
                if (idGrupo != null) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.desactivarUsuarioGrupo(usuarioGrupoRequest)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoHacerAdmin(idUsuario: Int, nombre: String, idGrupo: Int?) {
        Log.d(TAG, "Mostrando diálogo: Dar permisos de Administrador a ($nombre)")
        AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark)
            .setTitle("Dar permisos de Administrador")
            .setMessage("¿Quieres hacer a $nombre administrador? Tendrá permisos para modificar el grupo y expulsar a otros miembros.")
            .setPositiveButton("Confirmar") { _, _ ->
                Log.i(TAG, "Confirmado: Hacer administrador. idUsuario=$idUsuario, idGrupo=$idGrupo")
                if (idGrupo != null) {
                    val usuarioGrupoRequest = UsuarioGrupoRequest(idGrupo, idUsuario)
                    viewModel.hacerAdministrador(usuarioGrupoRequest)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}