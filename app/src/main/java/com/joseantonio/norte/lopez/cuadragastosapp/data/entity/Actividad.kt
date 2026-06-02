package com.joseantonio.norte.lopez.cuadragastosapp.data.entity

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.TipoActividad
import java.time.LocalDateTime

data class Actividad(
    var id: Int = 0,
    var grupo: Grupo,
    var descripcion: String,
    var tipo: TipoActividad?,
    var fecha: LocalDateTime
)