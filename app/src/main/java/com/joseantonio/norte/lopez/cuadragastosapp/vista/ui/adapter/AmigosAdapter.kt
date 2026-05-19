package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.UsuarioResponse

class AmigosAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder>() {

    private var listaAmigos = listOf<UsuarioResponse>()
    private val seleccionados = mutableSetOf<Int>()

    inner class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardViewPrincipal: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.cardViewPrincipal)
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

        // 1. Evaluar si el usuario está en tu lista de seleccionados
        val estaSeleccionado = seleccionados.contains(amigo.idUsuario)

        // 2. Aplicar el estado a la tarjeta para que el selector XML pinte el borde verde
        holder.cardViewPrincipal.isChecked = estaSeleccionado

        // 3. Controlar la visibilidad del check verde de la derecha
        holder.ivCheck.visibility = if (estaSeleccionado) View.VISIBLE else View.GONE

        // 4. Manejo del Click limpio
        holder.itemView.setOnClickListener {
            if (seleccionados.contains(amigo.idUsuario)) {
                seleccionados.remove(amigo.idUsuario)
            } else {
                seleccionados.add(amigo.idUsuario)
            }

            // Notificar el cambio para que se vuelva a pintar esta posición con su nuevo estado
            notifyItemChanged(position)
            onSelectionChanged(seleccionados.size)
        }
    }

    override fun getItemCount(): Int = listaAmigos.size

    fun actualizarLista(nuevaLista: List<UsuarioResponse>) {
        this.listaAmigos = nuevaLista
        notifyDataSetChanged()
    }
    fun limpiarSeleccionados(){
        seleccionados.clear()
        notifyDataSetChanged()
    }
    fun getIdsSeleccionados(): List<Int> = seleccionados.toList()
}