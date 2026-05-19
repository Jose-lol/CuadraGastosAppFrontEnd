package com.joseantonio.norte.lopez.cuadragastosapp.data.entity

data class UsuarioGrupo(
    val id: UsuarioGrupoId? = null,
    val usuario: Usuario,
    val activo: Boolean,
    val rol: String,
    val fechaAlta: String? = null
)