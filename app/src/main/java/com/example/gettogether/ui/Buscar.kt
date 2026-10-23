package com.example.gettogether.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gettogether.model.FiltrosBusqueda
import com.example.gettogether.model.TiposEvento
import com.example.gettogether.viewmodel.BuscarViewModel

/**
 * Pantalla "Buscar" del mockup: buscador + Filtros (Ubicación, Tipo, Cantidad asistentes,
 * Asistentes confirmados, Rango etario, Etiquetas).
 */
@Composable
fun Buscar(
    viewModel: BuscarViewModel,
    onVolver: () -> Unit,
    onVerEvento: (Int) -> Unit
) {
    val filtros by viewModel.filtros.collectAsState()
    val resultados = viewModel.resultados(filtros)
    var verFiltros by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Buscar", onVolver = onVolver)

        LazyColumn(
            contentPadding = PaddingPantalla,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CampoTexto(
                    valor = filtros.texto,
                    onCambio = { v -> viewModel.actualizar { it.copy(texto = v) } },
                    etiqueta = "Buscar eventos",
                    icono = Icons.Filled.Search
                )
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalButton(onClick = { verFiltros = !verFiltros }) {
                        Text("Filtros")
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            if (verFiltros) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = viewModel::limpiarFiltros) { Text("Limpiar filtros") }
                }
            }
            item {
                AnimatedVisibility(visible = verFiltros) {
                    PanelFiltros(filtros) { cambio -> viewModel.actualizar(cambio) }
                }
            }
            item {
                Text(
                    "${resultados.size} resultado(s)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(resultados, key = { it.id }) { evento ->
                EventoResumenCard(evento, onClick = { onVerEvento(evento.id) })
            }
        }
    }
}

@Composable
private fun PanelFiltros(
    filtros: FiltrosBusqueda,
    onCambio: ((FiltrosBusqueda) -> FiltrosBusqueda) -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            CampoTexto(
                valor = filtros.ubicacion,
                onCambio = { v -> onCambio { it.copy(ubicacion = v) } },
                etiqueta = "Ubicación",
                icono = Icons.Filled.LocationOn
            )

            Text("Tipo", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TiposEvento.lista) { tipo ->
                    FilterChip(
                        selected = filtros.tipo == tipo,
                        onClick = { onCambio { it.copy(tipo = if (it.tipo == tipo) null else tipo) } },
                        label = { Text(tipo) }
                    )
                }
            }

            val cupo = filtros.cupoMaximo.toInt()
            Text(
                "Cantidad asistentes (cupo máximo): ${if (cupo >= FiltrosBusqueda.CUPO_MAX.toInt()) "sin límite" else cupo}",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = filtros.cupoMaximo,
                onValueChange = { v -> onCambio { it.copy(cupoMaximo = v) } },
                valueRange = 2f..FiltrosBusqueda.CUPO_MAX
            )

            Text(
                "Asistentes confirmados (mínimo): ${filtros.minConfirmados.toInt()}",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = filtros.minConfirmados,
                onValueChange = { v -> onCambio { it.copy(minConfirmados = v) } },
                valueRange = 0f..50f
            )

            Text(
                "Rango etario: ${filtros.edadDesde.toInt()} - ${filtros.edadHasta.toInt()} años",
                style = MaterialTheme.typography.labelLarge
            )
            RangeSlider(
                value = filtros.edadDesde..filtros.edadHasta,
                onValueChange = { r -> onCambio { it.copy(edadDesde = r.start, edadHasta = r.endInclusive) } },
                valueRange = FiltrosBusqueda.EDAD_MIN..FiltrosBusqueda.EDAD_MAX
            )

            CampoTexto(
                valor = filtros.etiqueta,
                onCambio = { v -> onCambio { it.copy(etiqueta = v) } },
                etiqueta = "Etiquetas (ej: pokemon)"
            )
        }
    }
}
