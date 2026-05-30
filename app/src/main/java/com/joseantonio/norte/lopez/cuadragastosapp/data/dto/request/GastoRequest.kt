package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGasto
import java.math.BigDecimal
import java.time.LocalDateTime

data class GastoRequest(

    val idGrupo: Int? = null,
    val categoriaGasto: CategoriaGasto,
    val descripcion: String? = null,
    val cantidad: BigDecimal,
    val fecha: LocalDateTime? = null,
    val saldado: Boolean = false
)