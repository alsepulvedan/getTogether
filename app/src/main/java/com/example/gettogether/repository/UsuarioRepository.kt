package com.example.gettogether.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gettogether.model.Ajustes
import com.example.gettogether.model.Usuario
import com.example.gettogether.model.UsuarioDao
import com.example.gettogether.model.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UsuarioRepository(private val usuarioDao: UsuarioDao? = null) {

    // =========================================================
    // 1. SESIÓN CON ROOM (opcional: solo funciona si se entrega un dao)
    // =========================================================
    val usuarioActivo: Flow<UsuarioEntity?> = usuarioDao?.obtenerUsuarioActivo() ?: flowOf(null)

    suspend fun iniciarSesion(correo: String, nombre: String) {
        usuarioDao?.guardarSesion(UsuarioEntity(correo = correo, nombre = nombre))
    }

    suspend fun cerrarSesion() {
        Companion.usuarioActual = null
        usuarioDao?.cerrarSesion()
    }

    // =========================================================
    // 2. USUARIOS EN MEMORIA (luego se reemplaza por el microservicio)
    // =========================================================
    var usuarioActual: Usuario?
        get() = Companion.usuarioActual
        set(value) { Companion.usuarioActual = value }

    var ajustes: Ajustes
        get() = Companion.ajustes
        set(value) { Companion.ajustes = value }

    companion object {
        var usuarioActual: Usuario? = null

        // mutableStateOf para que Compose redibuje los switches de Ajustes
        var ajustes by mutableStateOf(Ajustes())

        // Usuarios registrados. "demo" ya está confirmado en los eventos 4, 5, 6 y 7.
        private val usuarios = mutableListOf(
            Usuario(
                nombre = "Usuario Demo",
                nombreUsuario = "demo",
                correo = "demo@gettogether.cl",
                password = "123456",
                ubicacion = "Santiago",
                edad = 25
            )
        )
    }

    /** Devuelve true y deja la sesión iniciada si el correo y la contraseña coinciden. */
    fun login(correo: String, pass: String): Boolean {
        val usuario = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.password == pass
        }
        usuarioActual = usuario
        return usuario != null
    }

    /** Guarda el usuario nuevo y deja la sesión iniciada. */
    fun registrar(u: Usuario) {
        usuarios.add(u)
        usuarioActual = u
    }

    fun existeNombreUsuario(nombre: String): Boolean =
        usuarios.any { it.nombreUsuario.equals(nombre.trim(), ignoreCase = true) }

    fun existeCorreo(correo: String): Boolean =
        usuarios.any { it.correo.equals(correo.trim(), ignoreCase = true) }

    /** Actualiza el perfil en la sesión y en la lista (se busca por nombre de usuario, que no se edita). */
    fun actualizarPerfil(u: Usuario) {
        val i = usuarios.indexOfFirst { it.nombreUsuario == u.nombreUsuario }
        if (i >= 0) usuarios[i] = u
        usuarioActual = u
    }

    fun actualizarAjustes(a: Ajustes) {
        ajustes = a
    }
}