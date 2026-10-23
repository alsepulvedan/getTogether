package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.EventoViewModel

/** Pantalla "Información evento 1" del mockup: Imágenes, Descripción, Ubicación, Etiquetas. */
@Composable
fun InformacionEvento(
    id: Int,
    viewModel: EventoViewModel,
    onVolver: () -> Unit,
    onIrAlEvento: () -> Unit
) {
    val evento = viewModel.obtenerEvento(id)

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Información", onVolver = onVolver)
        if (evento == null) {
            EventoNoEncontrado()
            return@Column
        }
        val confirmado = viewModel.estaConfirmado(evento)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingPantalla),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            EncabezadoEvento(evento)

            TituloSeccion("Imágenes")
            if (evento.imagenes.isEmpty()) {
                PlaceholderImagen("Sin imágenes", Modifier.fillMaxWidth().height(140.dp))
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(evento.imagenes) { uri ->
                        ImagenUri(uri, Modifier.size(160.dp).clip(RoundedCornerShape(12.dp)), evento.nombre)
                    }
                }
            }

            TituloSeccion("Descripción")
            Text(evento.descripcion)

            TituloSeccion("Ubicación")
            Text(evento.ubicacion)
            if (evento.referencia.isNotBlank()) {
                Text(evento.referencia, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            TituloSeccion("Etiquetas")
            if (evento.etiquetas.isEmpty()) Text("Sin etiquetas") else FilaEtiquetas(evento.etiquetas)

            TituloSeccion("Detalles")
            Text("Tipo: ${evento.tipo}")
            Text("Rango etario: ${evento.rangoEtario}")
            Text("Asistentes: ${evento.asistentes.size} de ${evento.cupo}")
            if (evento.creador.isNotBlank()) Text("Organiza: ${evento.creador}")

            Spacer(Modifier.height(16.dp))
            if (confirmado) {
                OutlinedButton(onClick = onIrAlEvento, modifier = Modifier.fillMaxWidth()) {
                    Text("Ir al chat y asistentes")
                }
            } else {
                Button(
                    onClick = { viewModel.unirse(id) },
                    enabled = evento.cuposDisponibles > 0,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(if (evento.cuposDisponibles > 0) "¡Me uno!" else "Sin cupos")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
