package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla "Asistentes Evento 1" del mockup (juan, luis, sofia, nicolas...). */
@Composable
fun AsistentesEvento(id: Int, viewModel: EventoViewModel, onVolver: () -> Unit) {
    val evento = viewModel.obtenerEvento(id)

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Asistentes", onVolver = onVolver)
        if (evento == null) {
            EventoNoEncontrado()
            return@Column
        }
        LazyColumn(
            contentPadding = PaddingPantalla,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { EncabezadoEvento(evento) }
            item {
                TituloSeccion("Asistentes (${evento.asistentes.size}/${evento.cupo})")
            }
            items(evento.asistentes) { nombre ->
                Card(Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text(if (nombre == viewModel.usuario) "$nombre (tú)" else nombre) },
                        supportingContent = if (nombre == evento.creador) {
                            { Text("Organizador") }
                        } else null,
                        leadingContent = { Avatar(nombre) }
                    )
                }
            }
        }
    }
}
