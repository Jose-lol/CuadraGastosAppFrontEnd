package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.response

import java.math.BigDecimal


data class SaldoUsuarioGrupo (
    val idUsuario: Int?,
    val nombre: String?,
    val totalPagado: BigDecimal,
    val saldoFinal: BigDecimal,
)