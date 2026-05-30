package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.GastoResponse
import java.time.format.DateTimeFormatter
import java.util.Locale

class GastoAdapter(
    private val listaGastos: List<GastoResponse>,
    private val onItemClick: (GastoResponse) -> Unit
) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    private val formatterMes = DateTimeFormatter.ofPattern("MMM", Locale("es", "ES"))
    private val formatterDia = DateTimeFormatter.ofPattern("dd")

    class GastoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvPagador: TextView = view.findViewById(R.id.tvPagador)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvMes: TextView = view.findViewById(R.id.tvMes)
        val tvDia: TextView = view.findViewById(R.id.tvDia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto, parent, false)
        return GastoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = listaGastos[position]

        holder.itemView.setOnClickListener {
            onItemClick(gasto)
        }

        holder.apply {
            tvDescripcion.text = gasto.descripcion ?: "Gasto sin nombre"
            tvPagador.text = "Pagó ${gasto.nombreUsuario ?: "Alguien"}"
            tvCantidad.text = "${gasto.cantidad ?: 0.0} €"

            // Formatear Fecha al estilo Splitwise
            gasto.fecha?.let {
                tvMes.text = it.format(formatterMes).replace(".", "")
                tvDia.text = it.format(formatterDia)
            }
        }
    }

    override fun getItemCount(): Int = listaGastos.size
}