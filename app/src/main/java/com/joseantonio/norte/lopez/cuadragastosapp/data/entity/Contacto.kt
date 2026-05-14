package com.joseantonio.norte.lopez.cuadragastosapp.data.entity

data class Contacto(
    val nombre: String,
    val telefono: String,
    var estaAgregado: Boolean = false
)