package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla del mockup con "Evento Confirmado 1" + Chat / Asistentes / Información. */
@Composable
fun DetalleEvento(
    id: Int,
    viewModel: EventoViewModel,
    onVolver: () -> Unit,
    onChat: () -> Unit,
    onAsistentes: () -> Unit,
    onInformacion: () -> Unit
) {
    val evento = viewModel.obtenerEvento(id)

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Evento", onVolver = onVolver)
        if (evento == null) {
            EventoNoEncontrado()
            return@Column
        }
        val ultimoMensaje = viewModel.mensajes(id).lastOrNull()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingPantalla),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EncabezadoEvento(evento)
            OpcionEvento(
                icono = Icons.Filled.Email,
                titulo = "Chat del evento",
                detalle = ultimoMensaje?.let { "${it.autor}: ${it.texto}" } ?: "Aún no hay mensajes",
                onClick = onChat
            )
            OpcionEvento(
                icono = Icons.Filled.Person,
                titulo = "Asistentes",
                detalle = "${evento.asistentes.size} de ${evento.cupo} confirmados",
                onClick = onAsistentes
            )
            OpcionEvento(
                icono = Icons.Filled.Info,
                titulo = "Información del evento",
                detalle = "${evento.ubicacion} · ${evento.rangoEtario}",
                onClick = onInformacion
            )

            Spacer(Modifier.height(8.dp))
            if (viewModel.estaConfirmado(evento)) {
                OutlinedButton(
                    onClick = {
                        viewModel.salir(id)
                        onVolver()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salir del evento", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun OpcionEvento(icono: ImageVector, titulo: String, detalle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(titulo, fontWeight = FontWeight.Bold) },
            supportingContent = { Text(detalle, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            leadingContent = { Icon(icono, contentDescription = null) },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun EventoNoEncontrado() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Evento no encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
