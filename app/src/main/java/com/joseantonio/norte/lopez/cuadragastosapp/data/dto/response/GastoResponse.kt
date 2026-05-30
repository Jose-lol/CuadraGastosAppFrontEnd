package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGasto
import java.math.BigDecimal
import java.time.LocalDateTime

data class GastoResponse(
    val idGasto: Int?,
    val descripcion: String?,
    val cantidad: BigDecimal?,
    val categoriaGasto: CategoriaGasto?,
    val fecha: LocalDateTime?,
    val saldado: Boolean,
    val idGrupo: Int?,
    val idUsuario: Int?,
    val nombreUsuario: String?
)