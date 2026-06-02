package com.joseantonio.norte.lopez.cuadragastosapp.vista.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseantonio.norte.lopez.cuadragastosapp.R
import com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response.ActividadResponse
import com.joseantonio.norte.lopez.cuadragastosapp.data.entity.Actividad
class ActividadAdapter(
    private val listaActividades: List<ActividadResponse>
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = listaActividades[position]
        holder.bind(actividad)
    }

    override fun getItemCount(): Int = listaActividades.size

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionActividad)

        fun bind(actividad: ActividadResponse) {
            tvDescripcion.text = actividad.descripcion
        }
    }
}