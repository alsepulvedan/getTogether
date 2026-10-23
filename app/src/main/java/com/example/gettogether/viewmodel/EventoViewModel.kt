package com.example.gettogether.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gettogether.model.Evento
import com.example.gettogether.model.Mensaje
import com.example.gettogether.repository.EventoRepository
import com.example.gettogether.repository.UsuarioRepository

/** Lógica de Inicio, Bandeja, detalle, chat, asistentes e información del evento. */
class EventoViewModel : ViewModel() {

    val usuario: String
        get() = UsuarioRepository.usuarioActual?.nombreUsuario ?: "invitado"

    val eventosDisponibles: List<Evento>
        get() = EventoRepository.eventosDisponibles(usuario)

    val eventosConfirmados: List<Evento>
        get() = EventoRepository.eventosConfirmados(usuario)

    fun obtenerEvento(id: Int): Evento? = EventoRepository.obtenerEvento(id)

    fun estaConfirmado(evento: Evento): Boolean = usuario in evento.asistentes

    fun unirse(id: Int) = EventoRepository.confirmarAsistencia(id, usuario)

    fun salir(id: Int) = EventoRepository.cancelarAsistencia(id, usuario)

    fun mensajes(id: Int): List<Mensaje> = EventoRepository.mensajes(id)

    fun esMensajePropio(mensaje: Mensaje): Boolean = mensaje.autor == usuario

    fun enviarMensaje(id: Int, texto: String): Boolean {
        if (texto.isBlank()) return false
        EventoRepository.enviarMensaje(id, usuario, texto.trim())
        return true
    }
}
