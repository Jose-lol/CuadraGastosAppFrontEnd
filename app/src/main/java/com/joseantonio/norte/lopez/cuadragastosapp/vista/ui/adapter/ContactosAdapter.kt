package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Contacto

class ContactosAdapter(
    private var contactos: List<Contacto>,
    private val onAddClick: (Contacto) -> Unit,
    private val onDeleteClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactosAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvName)
        val telefono: TextView = view.findViewById(R.id.tvPhone)
        val addBtn: MaterialButton = view.findViewById(R.id.btnAdd)
        val deleteBtn: MaterialButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = contactos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contacto = contactos[position]

        holder.nombre.text = contacto.nombre
        holder.telefono.text = contacto.telefono

        if (contacto.estaAgregado) {
            //Ya es amigo (Lista Superior)
            holder.addBtn.visibility = View.GONE
            holder.deleteBtn.visibility = View.VISIBLE

            holder.deleteBtn.setOnClickListener {
                onDeleteClick(contacto)
            }
        } else {
            //Contacto del móvil (Lista Inferior)
            holder.addBtn.visibility = View.VISIBLE
            holder.deleteBtn.visibility = View.GONE

            holder.addBtn.setOnClickListener {
                onAddClick(contacto)
            }
        }
    }

    fun updateLista(nuevaLista: List<Contacto>) {
        this.contactos = nuevaLista
        notifyDataSetChanged()
    }
}