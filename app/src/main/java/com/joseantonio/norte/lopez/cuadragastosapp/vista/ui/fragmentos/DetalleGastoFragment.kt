package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.databinding.FragmentDetalleGastoBinding
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetalleGastoFragment : Fragment(R.layout.fragment_detalle_gasto) {

    private var _binding: FragmentDetalleGastoBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "DetalleGastoFragment"
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleGastoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.gastoSeleccionado.collect { gasto ->
                    Log.d(TAG, "StateFlow gastoSeleccionado emitió: $gasto")
                    gasto?.let {
                        cargarDatosEnVista(it)
                    }
                }
            }
        }
    }

    private fun cargarDatosEnVista(gasto: com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse) {
        val formatterFecha = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy HH:mm", Locale("es", "ES"))

        Log.d(TAG, "cargarDatosEnVista: Pintando datos del gasto ID = ${gasto.idGasto}")
        binding.tvDetalleDescripcion.text = gasto.descripcion ?: "Sin descripción"
        binding.tvDetalleCantidad.text = "${gasto.cantidad ?: 0.0} €"
        binding.tvDetallePagador.text = gasto.nombreUsuario ?: "Desconocido"

        binding.tvDetalleCategoria.text = gasto.categoriaGasto?.name ?: "Sin categoría"

        gasto.fecha?.let {
            binding.tvDetalleFecha.text = it.format(formatterFecha)
        } ?: run {
            binding.tvDetalleFecha.text = "--/--/--"
            Log.w(TAG, "Advertencia: El gasto no tiene fecha asignada")
        }

        if (gasto.saldado) {
            Log.d(TAG, "Estado del gasto: Saldado")
            binding.tvDetalleEstado.text = "Saldado"
            binding.tvDetalleEstado.setTextColor(Color.parseColor("#5BC5A7"))
        } else {
            Log.d(TAG, "Estado del gasto: Pendiente")
            binding.tvDetalleEstado.text = "Pendiente"
            binding.tvDetalleEstado.setTextColor(Color.parseColor("#FF6B6B"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}