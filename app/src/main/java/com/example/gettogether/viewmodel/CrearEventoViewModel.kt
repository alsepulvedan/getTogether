package com.example.gettogether.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gettogether.model.Evento
import com.example.gettogether.model.EventoForm
import com.example.gettogether.model.EventoFormError
import com.example.gettogether.repository.EventoRepository
import com.example.gettogether.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CrearEventoViewModel : ViewModel() {

    private val _form = MutableStateFlow(EventoForm())
    val form: StateFlow<EventoForm> = _form

    private val _errores = MutableStateFlow(validar(EventoForm()))
    val errores: StateFlow<EventoFormError> = _errores

    // Los errores se muestran recién después del primer intento de enviar
    private val _mostrarErrores = MutableStateFlow(false)
    val mostrarErrores: StateFlow<Boolean> = _mostrarErrores

    fun onNombreChange(valor: String) = actualizar { it.copy(nombre = valor) }
    fun onTipoChange(valor: String) = actualizar { it.copy(tipo = if (it.tipo == valor) "" else valor) }
    fun onDescripcionChange(valor: String) = actualizar { it.copy(descripcion = valor) }
    fun onUbicacionChange(valor: String) = actualizar { it.copy(ubicacion = valor) }
    fun onEtiquetasChange(valor: String) = actualizar { it.copy(etiquetas = valor) }
    fun onEdadMinimaChange(valor: String) = actualizar { it.copy(edadMinima = valor.filter(Char::isDigit).take(2)) }
    fun onEdadMaximaChange(valor: String) = actualizar { it.copy(edadMaxima = valor.filter(Char::isDigit).take(2)) }
    fun onCupoChange(valor: String) = actualizar { it.copy(cupo = valor.filter(Char::isDigit).take(3)) }

    fun agregarImagenes(uris: List<String>) =
        actualizar { it.copy(imagenes = (it.imagenes + uris).distinct().take(MAX_IMAGENES)) }

    fun quitarImagen(uri: String) = actualizar { it.copy(imagenes = it.imagenes - uri) }

    /** Devuelve el evento creado, o null si el formulario tiene errores. */
    fun crearEvento(): Evento? {
        val errores = validar(_form.value)
        _errores.value = errores
        _mostrarErrores.value = true
        if (!errores.esValido) return null

        val creador = UsuarioRepository.usuarioActual?.nombreUsuario ?: "invitado"
        val evento = EventoRepository.crearEvento(_form.value, creador)
        limpiar()
        return evento
    }

    fun limpiar() {
        _form.value = EventoForm()
        _errores.value = validar(EventoForm())
        _mostrarErrores.value = false
    }

    private fun actualizar(cambio: (EventoForm) -> EventoForm) {
        _form.value = cambio(_form.value)
        _errores.value = validar(_form.value)
    }

    private fun validar(f: EventoForm): EventoFormError {
        val min = f.edadMinima.toIntOrNull()
        val max = f.edadMaxima.toIntOrNull()
        val cupo = f.cupo.toIntOrNull()
        return EventoFormError(
            nombre = when {
                f.nombre.isBlank() -> "El nombre es obligatorio"
                f.nombre.trim().length < 3 -> "Mínimo 3 caracteres"
                else -> null
            },
            tipo = if (f.tipo.isBlank()) "Selecciona un tipo de evento" else null,
            descripcion = if (f.descripcion.isBlank()) "Agrega una descripción" else null,
            ubicacion = if (f.ubicacion.isBlank()) "La ubicación es obligatoria" else null,
            edad = when {
                min == null || max == null -> "Ingresa ambas edades"
                min < 18 -> "La edad mínima es 18"
                max > 99 -> "La edad máxima es 99"
                min > max -> "La edad mínima no puede ser mayor que la máxima"
                else -> null
            },
            cupo = if (cupo == null || cupo !in 2..500) "El cupo debe estar entre 2 y 500" else null
        )
    }

    companion object {
        const val MAX_IMAGENES = 5
    }
}
