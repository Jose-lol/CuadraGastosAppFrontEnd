package com.joseantonio.norte.lopez.amediasApp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.GrupoResponse

class GrupoAdapter(private val listaGrupos: List<GrupoResponse>,private val onItemClick: (GrupoResponse) -> Unit) :
        RecyclerView.Adapter<GrupoAdapter.GroupViewHolder>() {

        class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val img: ImageView = view.findViewById(R.id.img)
            val txtNombreGrupo: TextView = view.findViewById(R.id.txtNombreGrupo)
            val txtEstado: TextView = view.findViewById(R.id.txtEstado)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_grupo, parent, false)
            return GroupViewHolder(view)
        }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
            val grupo = listaGrupos[position]

            // Configuramos el click en la raíz del ítem
            holder.itemView.setOnClickListener {
                onItemClick(grupo)
            }

            holder.apply {
                img.setImageResource(R.drawable.ic_launcher_foreground)
                txtNombreGrupo.text = grupo.nombre
                txtEstado.text = grupo.estado
            }
        }

        override fun getItemCount(): Int = listaGrupos.size

}