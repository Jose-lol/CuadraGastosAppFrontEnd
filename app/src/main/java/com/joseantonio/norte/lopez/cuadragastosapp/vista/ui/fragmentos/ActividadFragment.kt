package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ActividadRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.ActividadResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Actividad
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.ActividadAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.CargarActividadesViewModel
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class ActividadFragment : Fragment(R.layout.fragment_actividad) {

    companion object {
        private const val TAG = "ActividadFragment"
    }

    private lateinit var actividadAdapter: ActividadAdapter
    private var listaActividades = mutableListOf<ActividadResponse>()

    private val viewModel: CargarActividadesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = ActividadRepository(requireContext().applicationContext)
                return CargarActividadesViewModel(repo) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        setupRecyclerView(view)

        Log.d(TAG, "Llamando a viewModel.misActividades")
        viewModel.misActividades()

        viewModel.actividad.observe(viewLifecycleOwner) { nuevasActividades ->
            Log.d(TAG, "Observador actividad: Recibidas ${nuevasActividades?.size ?: 0} actividades")
            nuevasActividades?.let {
                this.listaActividades.clear()
                this.listaActividades.addAll(nuevasActividades)
                actividadAdapter.notifyDataSetChanged()
                Log.d(TAG, "Adapter de actividades actualizado")
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                Toast.makeText(requireContext(), "ERROR: $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        Log.d(TAG, "Inicializando RecyclerView de actividades")
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvActividades)

        actividadAdapter = ActividadAdapter(listaActividades)

        recyclerView.apply {
            adapter = actividadAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}