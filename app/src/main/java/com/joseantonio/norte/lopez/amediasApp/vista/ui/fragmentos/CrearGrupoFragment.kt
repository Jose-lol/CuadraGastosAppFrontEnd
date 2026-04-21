package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Build
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
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.Repository.GrupoRepository
import com.joseantonio.norte.lopez.amediasApp.data.dto.request.GrupoRequest
import com.joseantonio.norte.lopez.amediasApp.data.entity.Grupo
import com.joseantonio.norte.lopez.amediasApp.session.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.CrearGruposViewModel
import java.time.Clock
import java.time.LocalDateTime
import kotlin.getValue



class CrearGrupoFragment : Fragment(R.layout.fragment_crear_grupo){


    lateinit var clock: Clock
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
        val btnTipoCasa = view.findViewById<Button>(R.id.btnTipoCasa)
        val btnTipoViaje = view.findViewById<Button>(R.id.btnTipoViaje)
        val btnTipoOtro = view.findViewById<Button>(R.id.btnTipoOtro)
        val btnCrearGrupo = view.findViewById<Button>(R.id.btnCrearGrupo)
        val usuario= SessionManager.usuario

        btnCrearGrupo.setOnClickListener {

            val grupo = Grupo(
                nombre = txtNombreGrupo.text.toString(),
                estado = "Sin gasto",
                activo = true,
                fechaAlta = LocalDateTime.now(clock)
            )
            val grupoRequest = GrupoRequest(grupo)
            viewModel.crearGrupo(usuario.idUsuario,grupoRequest)
        }

        viewModel.grupo.observe(viewLifecycleOwner) { grupos ->
            grupos?.let {
                Toast.makeText(
                    requireContext(),
                    "grupo creado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
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