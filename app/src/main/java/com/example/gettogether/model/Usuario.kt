package com.example.gettogether.model

data class Usuario(
    val nombre: String = "",
    val nombreUsuario: String = "",
    val correo: String = "",
    val password: String = "",
    val ubicacion: String = "",
    val intereses: String = "",
    val edad: Int? = null,
    val telefono: String = "",
    val foto: String? = null // Uri de la foto de perfil
)

data class Ajustes(
    val notificaciones: Boolean = true,
    val perfilPublico: Boolean = true,
    val soloCercanos: Boolean = false
)
