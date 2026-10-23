package com.example.gettogether.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gettogether.model.TiposEvento
import com.example.gettogether.viewmodel.CrearEventoViewModel

/**
 * Pantalla "Crear" del mockup: nombre del evento, adjuntar imágenes + vista previa,
 * descripción, ubicación, etiquetas y edad.
 */
@Composable
fun CrearEvento(
    viewModel: CrearEventoViewModel,
    onVolver: () -> Unit,
    onCreado: (Int) -> Unit
) {
    val form by viewModel.form.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val mostrar by viewModel.mostrarErrores.collectAsState()
    var eventoCreadoId by remember { mutableStateOf<Int?>(null) }

    // Selector de fotos del sistema (no necesita permisos)
    val selectorImagenes = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(CrearEventoViewModel.MAX_IMAGENES)
    ) { uris -> viewModel.agregarImagenes(uris.map { it.toString() }) }

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Crear Evento", onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingPantalla),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CampoTexto(form.nombre, viewModel::onNombreChange, "Nombre del evento",
                error = errores.nombre, mostrarError = mostrar)

            // ----- Adjuntar imágenes / Imágenes -----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedCard(
                    onClick = {
                        selectorImagenes.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.size(width = 110.dp, height = 90.dp)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("Adjuntar\nimágenes", style = MaterialTheme.typography.labelMedium)
                    }
                }
                if (form.imagenes.isEmpty()) {
                    PlaceholderImagen("Imágenes", Modifier.weight(1f).height(90.dp))
                } else {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(form.imagenes) { uri ->
                            Box {
                                ImagenUri(uri, Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)))
                                FilledTonalIconButton(
                                    onClick = { viewModel.quitarImagen(uri) },
                                    modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Quitar", Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
            Text(
                "${form.imagenes.size}/${CrearEventoViewModel.MAX_IMAGENES} imágenes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // ----- Tipo -----
            TituloSeccion("Tipo de evento")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TiposEvento.lista) { tipo ->
                    FilterChip(
                        selected = form.tipo == tipo,
                        onClick = { viewModel.onTipoChange(tipo) },
                        label = { Text(tipo) }
                    )
                }
            }
            if (mostrar) TextoError(errores.tipo)

            Spacer(Modifier.height(4.dp))
            CampoTexto(form.descripcion, viewModel::onDescripcionChange, "Añadir descripción",
                error = errores.descripcion, mostrarError = mostrar, lineasMinimas = 3)
            CampoTexto(form.ubicacion, viewModel::onUbicacionChange, "Añadir ubicación",
                error = errores.ubicacion, mostrarError = mostrar, icono = Icons.Filled.LocationOn)
            CampoTexto(form.etiquetas, viewModel::onEtiquetasChange, "Añadir etiquetas (separadas por coma)")
            FilaEtiquetas(
                form.etiquetas.split(",").map { it.trim().removePrefix("#") }.filter { it.isNotEmpty() }
            )

            // ----- Edad -----
            TituloSeccion("Añadir edad")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoTexto(form.edadMinima, viewModel::onEdadMinimaChange, "Edad mínima",
                    modifier = Modifier.weight(1f), tipoTeclado = KeyboardType.Number)
                CampoTexto(form.edadMaxima, viewModel::onEdadMaximaChange, "Edad máxima",
                    modifier = Modifier.weight(1f), tipoTeclado = KeyboardType.Number)
            }
            if (mostrar) TextoError(errores.edad)

            CampoTexto(form.cupo, viewModel::onCupoChange, "Cupo de asistentes",
                error = errores.cupo, mostrarError = mostrar, tipoTeclado = KeyboardType.Number)

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { viewModel.crearEvento()?.let { eventoCreadoId = it.id } },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Crear evento")
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    eventoCreadoId?.let { id ->
        AlertDialog(
            onDismissRequest = { },
            title = { Text("¡Evento creado!") },
            text = { Text("Tu evento ya está publicado y aparece en tu Bandeja.") },
            confirmButton = {
                Button(onClick = {
                    eventoCreadoId = null
                    onCreado(id)
                }) { Text("Ver evento") }
            }
        )
    }
}
