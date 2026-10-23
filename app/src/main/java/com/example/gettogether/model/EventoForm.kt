package com.example.gettogether.model

/** Datos del formulario "Crear Evento" (los números se guardan como texto, igual que en los TextField). */
data class EventoForm(
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val ubicacion: String = "",
    val etiquetas: String = "",
    val edadMinima: String = "18",
    val edadMaxima: String = "99",
    val cupo: String = "10",
    val imagenes: List<String> = emptyList()
)

data class EventoFormError(
    val nombre: String? = null,
    val tipo: String? = null,
    val descripcion: String? = null,
    val ubicacion: String? = null,
    val edad: String? = null,
    val cupo: String? = null
) {
    val esValido: Boolean
        get() = listOf(nombre, tipo, descripcion, ubicacion, edad, cupo).all { it == null }
}
