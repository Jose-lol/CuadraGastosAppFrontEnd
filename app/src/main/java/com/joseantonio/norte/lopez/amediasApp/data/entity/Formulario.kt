package com.joseantonio.norte.lopez.amediasApp.data.entity

data class Formulario(
    // 1. Valoración general (RatingBar)
    val satisfaccionClinica: Int,

    // 2. Calidad por áreas (RadioGroup)
    val atencionAlCliente: Int,         // 1–5
    val valoracionProfesional: Int,     // 1–5

    // 3. ¿Cómo nos conociste? (Checkboxes)
    val porAmigos: Boolean,
    val porGoogle: Boolean,
    val porAnuncios: Boolean,
    val porFacebook: Boolean,

    // 4. Comentarios libres
    val comentarios: String = "",

    // 5. Datos del usuario
    val nombre: String = "",
    val email: String = ""
)