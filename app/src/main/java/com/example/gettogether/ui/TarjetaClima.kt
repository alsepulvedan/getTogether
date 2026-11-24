package com.example.gettogether.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gettogether.model.ClimaEstado
import com.example.gettogether.viewmodel.ClimaViewModel
import kotlin.math.roundToInt

/** Tarjeta con el clima actual en la ubicación del evento (API externa Open-Meteo vía Retrofit). */
@Composable
fun TarjetaClima(
    latitud: Double,
    longitud: Double,
    viewModel: ClimaViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    // Pide el clima al abrir la pantalla (y otra vez si cambian las coordenadas)
    LaunchedEffect(latitud, longitud) { viewModel.cargar(latitud, longitud) }

    Card(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (val e = estado) {
                is ClimaEstado.Cargando -> {
                    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(12.dp))
                    Text("Cargando clima…")
                }
                is ClimaEstado.Exito -> Column {
                    Text(
                        "${e.clima.temperatura.roundToInt()}°C · ${e.clima.descripcion}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Viento ${e.clima.viento.roundToInt()} km/h",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Fuente: Open-Meteo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                is ClimaEstado.Error -> {
                    Text(
                        e.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel.cargar(latitud, longitud) }) { Text("Reintentar") }
                }
            }
        }
    }
}