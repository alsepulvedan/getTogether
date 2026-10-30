package com.example.gettogether.repository

import com.example.gettogether.model.Ajustes
import com.example.gettogether.model.Usuario
import com.example.gettogether.model.UsuarioDao
import com.example.gettogether.model.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UsuarioRepository(private val usuarioDao: UsuarioDao? = null) {

    // =========================================================
    // 1. NUEVA LÓGICA DE ROOM
    // =========================================================
    val usuarioActivo: Flow<UsuarioEntity?> = usuarioDao?.obtenerUsuarioActivo() ?: flowOf(null)

    suspend fun iniciarSesion(correo: String, nombre: String) {
        val nuevoUsuario = UsuarioEntity(correo = correo, nombre = nombre)
        usuarioDao?.guardarSesion(nuevoUsuario)
    }

    suspend fun cerrarSesion() {
        usuarioDao?.cerrarSesion()
    }

    // =========================================================
    // 2. LÓGICA ANTIGUA
    // =========================================================
    var usuarioActual: Usuario?
        get() = Companion.usuarioActual
        set(value) { Companion.usuarioActual = value }

    var ajustes: Ajustes = Ajustes()

    companion object {
        var usuarioActual: Usuario? = null
    }

    fun login(correo: String, pass: String): Boolean {
        return true
    }

    fun registrar(u: Usuario) {
    }

    fun existeNombreUsuario(nombre: String): Boolean {
        return false
    }

    fun existeCorreo(correo: String): Boolean {
        return false
    }

    fun actualizarPerfil(u: Usuario) {
        usuarioActual = u
    }

    fun actualizarAjustes(a: Ajustes) {
        ajustes = a
    }
}
