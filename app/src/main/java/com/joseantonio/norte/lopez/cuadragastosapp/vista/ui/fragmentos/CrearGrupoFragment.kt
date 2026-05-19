package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
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

        val btnBack= view.findViewById<ImageButton>(R.id.btnBack)
        val txtNombreGrupo= view.findViewById<EditText>(R.id.txtNombreGrupo)
        val btnCrearGrupo = view.findViewById<Button>(R.id.btnCrearGrupo)
        val toggleGroupType = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupType)

        val clock=Clock.systemUTC()

        sessionManager = SessionManager(requireContext())
        val idUsuario = sessionManager.getIdUsuario()

        if(idUsuario != -1 ) {
            btnCrearGrupo.setOnClickListener {

                val nombre = txtNombreGrupo.text.toString()

                // 2. Usamos 'when' para determinar la categoría según el botón seleccionado
                val categoriaSeleccionada: CategoriaGrupo = when (toggleGroupType.checkedButtonId) {
                    R.id.btnTipoCasa -> CategoriaGrupo.CASA
                    R.id.btnTipoViaje -> CategoriaGrupo.VIAJE
                    R.id.btnTipoPareja -> CategoriaGrupo.PAREJA
                    R.id.btnTipoOtro -> CategoriaGrupo.OTRO
                    else -> CategoriaGrupo.OTRO // Por si acaso no hay ninguno marcado
                }
                if (nombre.isNotEmpty()) {
                    val grupoRequest = GrupoRequest(
                        idGrupo = null,
                        nombre = nombre,
                        categoria = categoriaSeleccionada
                    )
                    viewModel.crearGrupo(grupoRequest)
                } else {
                    txtNombreGrupo.error = "Escribe un nombre para el grupo"
                }
            }
        }else{
            Toast.makeText(requireContext(), "Usuario no encontrado en las preferencias" , Toast.LENGTH_SHORT).show()
        }

        viewModel.grupo.observe(viewLifecycleOwner) { grupos ->
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
            msg?.let {
                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_crear_grupo_to_fragmento_grupo_principal)
        }
    }
}