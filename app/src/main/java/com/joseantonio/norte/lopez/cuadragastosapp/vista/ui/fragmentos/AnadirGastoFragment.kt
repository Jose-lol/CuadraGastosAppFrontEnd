package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.GastoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.GastoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGasto
import com.joseantonio.norte.lopez.cuadragastosapp.data.local.SessionManager
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.AnadirGastoViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.getValue

class AnadirGastoFragment : Fragment(R.layout.fragment_anadir_gasto) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var sessionManager: SessionManager

    var idGrupo: Int? = null

    private val viewModel: AnadirGastoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = GastoRepository(requireContext().applicationContext)
                return AnadirGastoViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val txtDescripcion = view.findViewById<TextInputEditText>(R.id.txtDescripcion)
        val txtCantidad = view.findViewById<TextInputEditText>(R.id.txtCantidad)
        val btnCrearGasto = view.findViewById<Button>(R.id.btnCrearGasto)

        setupCategorias(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.miUsuarioEnGrupo.collect { grupo ->
                    grupo?.let {
                        idGrupo = it.grupo.idGrupo
                    }
                }
            }
        }

        sessionManager = SessionManager(requireContext())

        btnCrearGasto.setOnClickListener {

            val descripcion = txtDescripcion.text.toString().trim()
            val cantidadTexto = txtCantidad.text.toString().trim()

            if (descripcion.isEmpty()) {
                txtDescripcion.error = "Introduce una descripción"
                return@setOnClickListener
            }

            if (cantidadTexto.isEmpty()) {
                txtCantidad.error = "Introduce una cantidad"
                return@setOnClickListener
            }

            val chipGroup = view.findViewById<ChipGroup>(R.id.chipCategorias)
            val selectedChipId = chipGroup.checkedChipId

            if (selectedChipId == View.NO_ID) {
                Toast.makeText(requireContext(), "Selecciona una categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
            val categoriaSeleccionada = selectedChip.tag as CategoriaGasto

            val gastoRequest = GastoRequest(
                idGrupo = idGrupo,
                categoriaGasto = categoriaSeleccionada,
                descripcion = descripcion,
                cantidad = BigDecimal(cantidadTexto),
                fecha = LocalDateTime.now(),
                saldado = false
            )

            viewModel.anadirGasto(gastoRequest)
        }

        viewModel.resultado.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "Gasto añadido correctamente",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_fragmento_anadir_gasto_to_fragmento_detalle_grupo)
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setupCategorias(view: View) {

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipCategorias)

        CategoriaGasto.values().forEach { categoria ->

            val chip = Chip(requireContext()).apply {
                text = categoria.name.lowercase()
                    .replace("_", " ")
                    .replaceFirstChar { it.uppercase() }

                isCheckable = true
                isClickable = true
                tag = categoria

                setChipBackgroundColorResource(android.R.color.transparent)
            }

            chipGroup.addView(chip)
        }
    }
}