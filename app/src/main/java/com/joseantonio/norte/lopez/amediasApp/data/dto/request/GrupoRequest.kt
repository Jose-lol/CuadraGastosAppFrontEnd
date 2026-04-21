package com.joseantonio.norte.lopez.amediasApp.data.dto.request

import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Grupo

data class GrupoRequest(
    val grupo: Grupo,
    val usuario: UsuarioResponse? = null
)