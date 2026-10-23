package com.example.gettogether.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gettogether.model.Ajustes
import com.example.gettogether.model.Usuario

object UsuarioRepository {

    private var _usuarios by mutableStateOf(
        listOf(
            Usuario(
                nombre = "Usuario Demo",
                nombreUsuario = "demo",
                correo = "demo@gettogether.cl",
                password = "123456",
                ubicacion = "Santiago",
                intereses = "Juegos, música, trekking",
                edad = 22,
                telefono = "+56 9 1234 5678"
            )
        )
    )
    val usuarios: List<Usuario> get() = _usuarios

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    var ajustes by mutableStateOf(Ajustes())
        private set

    fun existeCorreo(correo: String): Boolean =
        _usuarios.any { it.correo.equals(correo.trim(), ignoreCase = true) }

    fun existeNombreUsuario(nombreUsuario: String): Boolean =
        _usuarios.any { it.nombreUsuario.equals(nombreUsuario.trim(), ignoreCase = true) }

    fun login(correo: String, password: String): Boolean {
        val usuario = _usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.password == password
        }
        usuarioActual = usuario
        return usuario != null
    }

    fun registrar(usuario: Usuario) {
        _usuarios = _usuarios + usuario
        usuarioActual = usuario
    }

    fun actualizarPerfil(usuario: Usuario) {
        val anterior = usuarioActual ?: return
        _usuarios = _usuarios.map { if (it.correo == anterior.correo) usuario else it }
        usuarioActual = usuario
    }

    fun actualizarAjustes(nuevos: Ajustes) {
        ajustes = nuevos
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
