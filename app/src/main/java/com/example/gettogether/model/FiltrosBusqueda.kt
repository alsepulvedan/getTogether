package com.example.gettogether.model

data class FiltrosBusqueda(
    val texto: String = "",
    val ubicacion: String = "",
    val tipo: String? = null,
    val cupoMaximo: Float = CUPO_MAX,          // "Cantidad asistentes"
    val minConfirmados: Float = 0f,            // "Asistentes confirmados"
    val edadDesde: Float = EDAD_MIN,           // "Rango etario"
    val edadHasta: Float = EDAD_MAX,
    val etiqueta: String = ""                  // "Etiquetas"
) {
    companion object {
        const val CUPO_MAX = 500f
        const val EDAD_MIN = 18f
        const val EDAD_MAX = 99f
    }
}
