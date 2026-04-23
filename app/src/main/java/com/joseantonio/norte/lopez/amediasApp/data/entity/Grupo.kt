package com.joseantonio.norte.lopez.amediasApp.data.entity

import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import java.time.LocalDateTime

data class Grupo (

    var id: Int? = 0,
    val nombre: String? = "",
    val estado: String? = "",
    val activo: Boolean? = false,
    val fechaInactividad: LocalDateTime? = null ,
    val fechaAlta: LocalDateTime ,
    )