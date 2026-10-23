package com.example.gettogether.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gettogether.model.Evento
import com.example.gettogether.ui.theme.VerdeEvento
import com.example.gettogether.ui.theme.VerdeEventoTexto
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla 3 del mockup: Ajustes (izq), Crear (der), tarjeta grande del evento y barra inferior. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Inicio(
    viewModel: EventoViewModel,
    onAjustes: () -> Unit,
    onCrear: () -> Unit,
    onVerInfo: (Int) -> Unit
) {
    val eventos = viewModel.eventosDisponibles

    Column(Modifier.fillMaxSize()) {
        BarraSuperior(
            titulo = "Get Together",
            iconoIzquierdo = {
                IconButton(onClick = onAjustes) { Icon(Icons.Filled.Settings, contentDescription = "Ajustes") }
            },
            acciones = {
                IconButton(onClick = onCrear) { Icon(Icons.Filled.Add, contentDescription = "Crear evento") }
            }
        )

        if (eventos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No hay más eventos por ahora", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Revisa tu Bandeja o crea un evento nuevo",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onCrear) { Text("Crear evento") }
            }
            return@Column
        }

        val pagerState = rememberPagerState(pageCount = { eventos.size })

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.weight(1f)
        ) { pagina ->
            val evento = eventos.getOrNull(pagina) ?: return@HorizontalPager
            TarjetaEvento(
                evento = evento,
                onUnirse = { viewModel.unirse(evento.id) },
                onVerInfo = { onVerInfo(evento.id) }
            )
        }

        Text(
            "${(pagerState.currentPage + 1).coerceAtMost(eventos.size)} de ${eventos.size} · desliza para ver más",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}

/** Tarjeta verde "Evento" del mockup: imagen arriba y la info abajo. */
@Composable
private fun TarjetaEvento(evento: Evento, onUnirse: () -> Unit, onVerInfo: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = VerdeEvento, contentColor = VerdeEventoTexto),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(12.dp)) {
            PortadaEvento(evento, Modifier.fillMaxWidth().weight(1f))

            Spacer(Modifier.height(12.dp))

            // Bloque de información (cuadro blanco del mockup)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.75f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(evento.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(evento.ubicacion, style = MaterialTheme.typography.bodyLarge)
                }
                if (evento.referencia.isNotBlank()) {
                    Text(evento.referencia, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    evento.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${evento.asistentes.size}/${evento.cupo} asistentes · ${evento.rangoEtario}",
                    style = MaterialTheme.typography.bodySmall
                )
                FilaEtiquetas(evento.etiquetas)
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onVerInfo, modifier = Modifier.weight(1f)) { Text("Ver info") }
                Button(
                    onClick = onUnirse,
                    enabled = evento.cuposDisponibles > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (evento.cuposDisponibles > 0) "¡Me uno!" else "Sin cupos")
                }
            }
        }
    }
}
