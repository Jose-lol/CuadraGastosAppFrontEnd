package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioGrupoResponse

class MiembrosConfigAdapter(
    private val miId: Int?,
    private val onEliminarClick: (Int, String) -> Unit,
    private val onHacerAdminClick: (Int, String) -> Unit
) : RecyclerView.Adapter<MiembrosConfigAdapter.MiembroViewHolder>() {

    private var listaMiembros: List<UsuarioGrupoResponse> = emptyList()
    private var soyAdmin: Boolean = false

    fun submitList(lista: List<UsuarioGrupoResponse>) {
        this.listaMiembros = lista
        notifyDataSetChanged()
    }

    fun setSoyAdmin(esAdmin: Boolean) {
        this.soyAdmin = esAdmin
        notifyDataSetChanged()
    }

    inner class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreMiembro: TextView = itemView.findViewById(R.id.tvNombreMiembro)
        val tvRol: TextView = itemView.findViewById(R.id.tvRol)
        val llAccionesAdmin: LinearLayout = itemView.findViewById(R.id.llAccionesAdmin)
        val btnHacerAdmin: ImageButton = itemView.findViewById(R.id.btnHacerAdmin)
        val btnEliminarMiembro: ImageButton = itemView.findViewById(R.id.btnEliminarMiembro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_miembro_config, parent, false)
        return MiembroViewHolder(view)
    }

    override fun getItemCount(): Int = listaMiembros.size

    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembro = listaMiembros[position]

        holder.tvNombreMiembro.text = miembro.nombre

        if (miembro.rol == "ADMINISTRADOR") {
            holder.tvRol.text = "Administrador"
            holder.tvRol.setTextColor(android.graphics.Color.parseColor("#FFC107"))
        } else {
            holder.tvRol.text = "USUARIO"
            holder.tvRol.setTextColor(android.graphics.Color.parseColor("#5BC5A7"))
        }

        if (soyAdmin && miembro.idUsuario != miId) {
            holder.llAccionesAdmin.visibility = View.VISIBLE

            holder.btnHacerAdmin.visibility = if (miembro.rol == "ADMINISTRADOR") View.GONE else View.VISIBLE
        } else {
            holder.llAccionesAdmin.visibility = View.GONE
        }

        holder.btnEliminarMiembro.setOnClickListener {
            onEliminarClick(miembro.idUsuario, miembro.nombre)
        }

        holder.btnHacerAdmin.setOnClickListener {
            onHacerAdminClick(miembro.idUsuario, miembro.nombre)
        }
    }
}