package com.example.gettogether.model

data class RegistroForm(
    val nombre: String = "",
    val nombreUsuario: String = "",
    val correo: String = "",
    val edad: String = "",
    val password: String = "",
    val confirmar: String = "",
    val aceptaTerminos: Boolean = false
)

data class RegistroFormError(
    val nombre: String? = null,
    val nombreUsuario: String? = null,
    val correo: String? = null,
    val edad: String? = null,
    val password: String? = null,
    val confirmar: String? = null,
    val terminos: String? = null
) {
    val esValido: Boolean
        get() = listOf(nombre, nombreUsuario, correo, edad, password, confirmar, terminos).all { it == null }
}
