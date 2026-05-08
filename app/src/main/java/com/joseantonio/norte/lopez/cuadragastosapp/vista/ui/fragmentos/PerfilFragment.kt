package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.joseantonio.norte.lopez.cuadragastosapp.R


class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos ViewModel con context seguro
        //viewModel = UpdateClienteViewModel(DatabaseHelper(requireContext()))

        // Referencias a los elementos de la vista
        val txtNombre = view.findViewById<TextView>(R.id.txtNombre2)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefono2)
        val txtCiudad = view.findViewById<TextView>(R.id.txtCiudad2)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail2)
        val txtDireccion = view.findViewById<TextView>(R.id.txtDireccion2)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnIrModificar = view.findViewById<Button>(R.id.btnIrModificar)



        // Navegar a modificar perfil
        btnIrModificar.setOnClickListener {
            findNavController().navigate(R.id.action_fragmento_perfil_to_fragmento_modificar_perfil)
        }

    }
}