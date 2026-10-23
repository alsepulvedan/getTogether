package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gettogether.model.Mensaje
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla "Chat evento 1" del mockup. */
@Composable
fun ChatEvento(id: Int, viewModel: EventoViewModel, onVolver: () -> Unit) {
    val evento = viewModel.obtenerEvento(id)
    val mensajes = viewModel.mensajes(id)
    var texto by remember { mutableStateOf("") }
    val estadoLista = rememberLazyListState()

    // Baja al último mensaje cuando llega uno nuevo
    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) estadoLista.animateScrollToItem(mensajes.size - 1)
    }

    Column(Modifier.fillMaxSize().imePadding()) {
        BarraSuperior("Chat", onVolver = onVolver)
        if (evento == null) {
            EventoNoEncontrado()
            return@Column
        }
        EncabezadoEvento(evento, Modifier.padding(horizontal = 16.dp))

        LazyColumn(
            state = estadoLista,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingPantalla,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mensajes) { mensaje ->
                Burbuja(mensaje, esPropio = viewModel.esMensajePropio(mensaje))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                placeholder = { Text("Escribe un mensaje") },
                modifier = Modifier.weight(1f),
                maxLines = 3
            )
            FilledIconButton(
                onClick = { if (viewModel.enviarMensaje(id, texto)) texto = "" },
                enabled = texto.isNotBlank(),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
            }
        }
    }
}

@Composable
private fun Burbuja(mensaje: Mensaje, esPropio: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esPropio) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (esPropio) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (!esPropio) {
                    Text(mensaje.autor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
                Text(mensaje.texto)
            }
        }
    }
}
