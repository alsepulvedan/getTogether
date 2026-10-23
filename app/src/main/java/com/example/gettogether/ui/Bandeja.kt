package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla "Bandeja" del mockup: lista de eventos confirmados. */
@Composable
fun Bandeja(
    viewModel: EventoViewModel,
    onVolver: () -> Unit,
    onVerEvento: (Int) -> Unit
) {
    val eventos = viewModel.eventosConfirmados

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Bandeja", onVolver = onVolver)

        if (eventos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Aún no te unes a ningún evento.\nDesliza en Inicio y presiona \"¡Me uno!\"",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingPantalla,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(eventos, key = { it.id }) { evento ->
                    EventoResumenCard(evento, onClick = { onVerEvento(evento.id) })
                }
            }
        }
    }
}
