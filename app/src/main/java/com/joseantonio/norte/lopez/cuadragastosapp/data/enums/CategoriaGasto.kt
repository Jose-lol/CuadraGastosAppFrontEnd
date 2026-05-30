package com.joseantonio.norte.lopez.cuadragastosapp.data.enums

enum class CategoriaGasto(
    val icono: String,
    val colorHex: String
) {

    SUPERMERCADO("shopping-cart", "#4CAF50"),
    ALQUILER_HIPOTECA("home", "#795548"),
    LUZ("zap", "#FFEB3B"),
    AGUA("droplet", "#2196F3"),
    INTERNET_TELEFONO("wifi", "#03A9F4"),
    GAS_CALEFACCION("flame", "#FF9800"),
    COCHE("car", "#607D8B"),
    GASOLINA("fuel", "#FF5722"),
    RESTAURANTES_BARES("utensils", "#E91E63"),
    CINE_CONCIERTOS("film", "#9C27B0"),
    VIAJES_HOTELES("plane", "#00BCD4"),
    DELIVERY("bike", "#FFC107"),
    SUSCRIPCIONES("credit-card", "#3F51B5"),
    REGALOS("gift", "#E81E63"),
    ROPA("shirt", "#673AB7"),
    FARMACIA_MEDICINA("pill", "#009688"),
    PELUQUERIA_ESTETICA("scissors", "#E91E63"),
    SALUD_DENTAL("smile", "#4CAF50"),
    COLEGIO("graduation-cap", "#3F51B5"),
    GUARDERIA("baby", "#FF4081"),
    ACTIVIDADES_EXTRAESCOLARES("activity", "#4CAF50"),
    JUGUETES_PAÑALES("package", "#FF9800"),
    OTROS("help-circle", "#9E9E9E")
}