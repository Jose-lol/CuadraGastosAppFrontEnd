package com.joseantonio.norte.lopez.amediasApp.data.dto.request

import com.joseantonio.norte.lopez.amediasApp.data.dto.response.UsuarioResponse
import com.joseantonio.norte.lopez.amediasApp.data.entity.Grupo
import java.time.LocalDateTime

data class GrupoRequest(
    var idGrupo: Int? = null,
    var nombre: String? = "",
    var estado: String? = "",
    var activo: Boolean? = false,
    var fechaAlta: String? = null,
    var idUsuario: Int? = null,

)