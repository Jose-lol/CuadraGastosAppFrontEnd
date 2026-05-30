package com.joseantonio.norte.lopez.cuadragastosapp.data.entity

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGasto
import java.math.BigDecimal
import java.time.LocalDateTime

data class Gasto(
    val idGasto: Int? = null,
    val grupo: Grupo,
    val usuario: Usuario,
    val categoria: CategoriaGasto,
    val descripcion: String,
    val cantidad: BigDecimal,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val participantes: Set<Usuario> = emptySet()
)