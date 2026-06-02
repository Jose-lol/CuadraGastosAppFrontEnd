package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.Repository.ContactoRepository
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request.ContactoRequest
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto
import com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter.ContactosAdapter
import com.joseantonio.norte.lopez.cuadragastosapp.vista.viewmodel.ContactosViewModel
import kotlin.getValue

class ContactosFragment : Fragment(R.layout.fragment_contactos) {

    companion object {
        private const val TAG = "ContactosFragment"
    }

    private lateinit var adapterAmigos: ContactosAdapter
    private lateinit var adapterMovil: ContactosAdapter
    private var listaAmigos = mutableListOf<Contacto>()
    private var listaMovil = mutableListOf<Contacto>()

    private val viewModel: ContactosViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = ContactoRepository(requireContext().applicationContext)
                return ContactosViewModel(repo) as T
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, "Resultado de solicitud de permiso READ_CONTACTS: granted = $isGranted")
        if (isGranted) {
            viewModel.sincronizarContactosMovil(requireContext())
        } else {
            Log.w(TAG, "Permiso READ_CONTACTS denegado por el usuario")
            Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment cargado")

        val btnSincronizar = view.findViewById<Button>(R.id.btnSincronizar)
        val tvTituloAmigos = view.findViewById<TextView>(R.id.tvTituloAmigos)
        val tvTituloMovil = view.findViewById<TextView>(R.id.tvTituloMovil)

        setupRecyclerViews(view)

        Log.d(TAG, "Llamando a viewModel.cargarAmigosApp")
        viewModel.cargarAmigosApp()

        btnSincronizar.setOnClickListener {
            Log.i(TAG, "Click en btnSincronizar: Desactivando botón e iniciando gestión")
            btnSincronizar.isEnabled = false
            gestionarSincronizacion()
        }

        viewModel.contactosApp.observe(viewLifecycleOwner) { amigos ->
            Log.d(TAG, "Observador contactosApp: Recibidos ${amigos?.size ?: 0} amigos de la app")
            listaAmigos.clear()
            listaAmigos.addAll(amigos)
            adapterAmigos.notifyDataSetChanged()
            tvTituloAmigos.visibility = if (amigos.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.contactosMovil.observe(viewLifecycleOwner) { movil ->
            Log.d(TAG, "Observador contactosMovil: Recibidos ${movil?.size ?: 0} contactos del móvil")
            listaMovil.clear()
            listaMovil.addAll(movil)
            adapterMovil.notifyDataSetChanged()

            view.findViewById<RecyclerView>(R.id.recyclerContactosMovil).requestLayout()
            tvTituloMovil.visibility = if (movil.isNotEmpty()) View.VISIBLE else View.GONE
            btnSincronizar.isEnabled = true
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "Observador error: Mensaje de error recibido -> '$msg'")
            msg?.let {
                btnSincronizar.isEnabled = true
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gestionarSincronizacion() {
        val permiso = Manifest.permission.READ_CONTACTS
        val estadoPermiso = ContextCompat.checkSelfPermission(requireContext(), permiso)
        Log.d(TAG, "gestionarSincronizacion: Comprobando estado del permiso READ_CONTACTS")

        if (estadoPermiso == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permiso ya concedido previamente. Sincronizando...")
            viewModel.sincronizarContactosMovil(requireContext())
        } else {
            Log.i(TAG, "Permiso no concedido. Lanzando cuadro de diálogo del sistema")
            requestPermissionLauncher.launch(permiso)
        }
    }

    private fun setupRecyclerViews(view: View) {
        Log.d(TAG, "Inicializando RecyclerViews de amigos y móvil")
        val rvAmigos = view.findViewById<RecyclerView>(R.id.recyclerAmigosApp)
        val rvMovil = view.findViewById<RecyclerView>(R.id.recyclerContactosMovil)

        adapterAmigos = ContactosAdapter(listaAmigos,
            onAddClick = {},
            onDeleteClick = { contacto ->
                Log.i(TAG, "Click en eliminar contacto de la app: ${contacto.telefono}")
                viewModel.eliminarContacto(ContactoRequest(contacto.telefono))
            }
        )
        rvAmigos.layoutManager = LinearLayoutManager(requireContext())
        rvAmigos.adapter = adapterAmigos
        rvAmigos.isNestedScrollingEnabled = false

        adapterMovil = ContactosAdapter(listaMovil,
            onAddClick = { contacto ->
                Log.i(TAG, "Click en añadir contacto desde el móvil: ${contacto.telefono}")
                viewModel.anadirContacto(ContactoRequest(contacto.telefono))
            },
            onDeleteClick = {}
        )
        rvMovil.layoutManager = LinearLayoutManager(requireContext())
        rvMovil.adapter = adapterMovil
        rvMovil.isNestedScrollingEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Limpiando vista")
    }
}

