package com.joseantonio.norte.lopez.amediasApp.data.entity

import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse

data class Grupo (

    var id: Int? = 0,
    val nombre: String? = "",
    val estado: String? = "",
    val miembros: List<UsuarioResponse>? = null,
    )