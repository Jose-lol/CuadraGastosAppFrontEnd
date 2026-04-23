package com.joseantonio.norte.lopez.amediasApp.vista.ui.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.amediasApp.R
import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse

class AmigosAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder>() {

    private var listaAmigos = listOf<UsuarioResponse>()
    private val seleccionados = mutableSetOf<Int>()

    inner class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val ivCheck: ImageView = itemView.findViewById(R.id.ivCheck)
        val root: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amigo_seleccionable, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = listaAmigos[position]
        holder.tvNombre.text = amigo.nombre

        val estaSeleccionado = seleccionados.contains(amigo.idUsuario)

        if (estaSeleccionado) {
            holder.root.setBackgroundColor(Color.parseColor("#E8F5F1"))
            holder.ivCheck.visibility = View.VISIBLE
        } else {
            holder.root.setBackgroundColor(Color.WHITE)
            holder.ivCheck.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (seleccionados.contains(amigo.idUsuario)) {
                seleccionados.remove(amigo.idUsuario)
            } else {
                seleccionados.add(amigo.idUsuario)
            }
            notifyItemChanged(position)
            onSelectionChanged(seleccionados.size)
        }
    }

    override fun getItemCount(): Int = listaAmigos.size

    fun actualizarLista(nuevaLista: List<UsuarioResponse>) {
        this.listaAmigos = nuevaLista
        notifyDataSetChanged()
    }

    fun getIdsSeleccionados(): List<Int> = seleccionados.toList()
}