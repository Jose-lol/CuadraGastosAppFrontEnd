package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGrupo
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.CrearGruposViewModel
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.getValue



class CrearGrupoFragment : Fragment(R.layout.fragment_crear_grupo){

    companion object {
        private const val TAG = "CrearGrupoFragment"
    }

    lateinit var usuario : UsuarioResponse
    lateinit var sessionManager: SessionManager
    private val viewModel: CrearGruposViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = GrupoRepository(requireContext().applicationContext)
                return CrearGruposViewModel(repo) as T
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val btnBack= view.findViewById<ImageButton>(R.id.btnBack)
        val txtNombreGrupo= view.findViewById<EditText>(R.id.txtNombreGrupo)
        val btnCrearGrupo = view.findViewById<Button>(R.id.btnCrearGrupo)
        val toggleGroupType = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupType)

        val clock=Clock.systemUTC()

        sessionManager = SessionManager(requireContext())
        val idUsuario = sessionManager.getIdUsuario()
        Log.d(TAG, "Comprobando ID de usuario en sesión: $idUsuario")

        if(idUsuario != -1 ) {
            btnCrearGrupo.setOnClickListener {

                val nombre = txtNombreGrupo.text.toString()

                val categoriaSeleccionada: CategoriaGrupo = when (toggleGroupType.checkedButtonId) {
                    R.id.btnTipoCasa -> CategoriaGrupo.CASA
                    R.id.btnTipoViaje -> CategoriaGrupo.VIAJE
                    R.id.btnTipoPareja -> CategoriaGrupo.PAREJA
                    R.id.btnTipoOtro -> CategoriaGrupo.OTRO
                    else -> CategoriaGrupo.OTRO
                }

                Log.i(TAG, "Click en btnCrearGrupo: Intentando crear grupo '$nombre' con categoría '$categoriaSeleccionada'")

                if (nombre.isNotEmpty()) {
                    val grupoRequest = GrupoRequest(
                        idGrupo = null,
                        nombre = nombre,
                        categoria = categoriaSeleccionada
                    )
                    Log.d(TAG, "Campos válidos. Llamando a viewModel.crearGrupo")
                    viewModel.crearGrupo(grupoRequest)
                } else {
                    Log.w(TAG, "Validación fallida: El nombre del grupo está vacío")
                    txtNombreGrupo.error = "Escribe un nombre para el grupo"
                }
            }
        }else{
            Log.e(TAG, "Error: idUsuario es -1. No se encontró el usuario en las preferencias")
            Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
        }

        viewModel.grupo.observe(viewLifecycleOwner) { grupos ->
            Log.d(TAG, "Observador grupo: Grupo creado correctamente. Resultado = $grupos")
            grupos?.let {
                Toast.makeText(
                    requireContext(),
                    "grupo creado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_fragmento_crear_grupo_to_fragmento_grupo_principal)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnBack.setOnClickListener {
            Log.i(TAG, "Click en btnBack: Volviendo al grupo principal")
            findNavController().navigate(R.id.action_fragmento_crear_grupo_to_fragmento_grupo_principal)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}