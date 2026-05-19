package com.joseantonio.norte.lopez.cuadragastosapp.data.entity

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGrupo
import java.time.LocalDateTime

data class Grupo (

    var id: Int? = 0,
    val nombre: String? = "",
    val estado: String? = "",
    val categoria: CategoriaGrupo? = null,
    val activo: Boolean? = false,
    val fechaInactividad: LocalDateTime? = null,
    val fechaAlta: LocalDateTime,
    )