package com.example.gettogether.model

/**
 * Evento ("junta") que se muestra en Inicio, Buscar, Bandeja y en el detalle.
 * Un usuario está "confirmado" en el evento si su nombre de usuario está en [asistentes].
 */
data class Evento(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val ubicacion: String,
    val referencia: String = "",
    val etiquetas: List<String> = emptyList(),
    val edadMinima: Int = 18,
    val edadMaxima: Int = 99,
    val cupo: Int = 20,
    val asistentes: List<String> = emptyList(),
    val imagenes: List<String> = emptyList(),       // Uris de imágenes adjuntas desde el celular
    val portadaRes: Int? = null,                    // Imagen de la tarjeta (Inicio, Bandeja, Buscar)
    val imagenesRes: List<Int> = emptyList(),       // Imágenes de res/drawable (R.drawable.xxx)
    val latitud: Double? = null,                    // Coordenadas para el mapa
    val longitud: Double? = null,
    val creador: String = ""
) {
    val tieneCoordenadas: Boolean get() = latitud != null && longitud != null

    val rangoEtario: String get() = "$edadMinima - $edadMaxima años"
    val cuposDisponibles: Int get() = (cupo - asistentes.size).coerceAtLeast(0)
}

object TiposEvento {
    val lista = listOf("Juegos", "Deporte", "Música", "Comida", "Estudio", "Aire libre", "Otro")
}
