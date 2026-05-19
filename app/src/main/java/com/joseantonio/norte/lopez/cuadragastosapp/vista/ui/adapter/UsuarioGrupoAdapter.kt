package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse

class UsuarioGrupoAdapter(private val listaGrupos: List<UsuarioGrupoResponse>, private val onItemClick: (UsuarioGrupoResponse) -> Unit) :
        RecyclerView.Adapter<UsuarioGrupoAdapter.GroupViewHolder>() {

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
            val usuarioGrupo = listaGrupos[position]

            holder.itemView.setOnClickListener {
                onItemClick(usuarioGrupo)
            }

            holder.apply {
                img.setImageResource(R.drawable.ic_launcher_foreground)
                txtNombreGrupo.text = usuarioGrupo.grupo.nombre
                txtEstado.text = usuarioGrupo.grupo.estado
            }
        }

        override fun getItemCount(): Int = listaGrupos.size

}