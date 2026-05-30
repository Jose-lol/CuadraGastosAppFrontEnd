package com.joseantonio.norte.lopez.cuadragastosapp.data.dto.request

import com.joseantonio.norte.lopez.cuadragastosapp.data.enums.CategoriaGrupo

data class GrupoRequest(
    var idGrupo: Int? = null,
    var nombre: String? = "",
    var categoria: CategoriaGrupo? = null,
)