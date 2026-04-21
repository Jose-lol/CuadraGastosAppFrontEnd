package com.joseantonio.norte.lopez.amediasApp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.entity.Usuario
import com.joseantonio.norte.lopez.amediasApp.session.SessionManager
import com.joseantonio.norte.lopez.amediasApp.vista.viewmodel.UpdateClienteViewModel

class ModificarPerfilFragment : Fragment(R.layout.fragment_modificar_perfil) {

    private lateinit var viewModel: UpdateClienteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos ViewModel con context seguro
       // viewModel = UpdateClienteViewModel(DatabaseHelper(requireContext()))

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefono)
        val etCiudad = view.findViewById<EditText>(R.id.etCiudad)
        val etDireccion = view.findViewById<EditText>(R.id.etDireccion)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnGuardar.setOnClickListener {
            val clienteOriginal = SessionManager.usuario
            if (clienteOriginal == null) {
                Toast.makeText(requireContext(), "No se pudo obtener el cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usamos valores de EditText o mantenemos los originales si están vacíos
            val nombre = etNombre.text.toString().trim().ifEmpty { clienteOriginal.nombre }
            //val telefono = etTelefono.text.toString().trim().ifEmpty { clienteOriginal.telefono }
            //val direccion = etDireccion.text.toString().trim().ifEmpty { clienteOriginal.direccion }
            //val ciudad = etCiudad.text.toString().trim().ifEmpty { clienteOriginal.ciudad }


            viewModel.updateUsuarioResult.observe(viewLifecycleOwner) { updatedCliente ->
                if (updatedCliente != null) {
                    Toast.makeText(requireContext(), "Cliente actualizado", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_fragmento_modificar_perfil_to_fragmento_perfil)
                } else {
                    Toast.makeText(requireContext(), "No se pudo modificar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_modificar_perfil_to_fragmento_perfil)
        }
    }
}