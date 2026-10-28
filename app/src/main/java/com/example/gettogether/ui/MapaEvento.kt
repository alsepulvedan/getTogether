package com.example.gettogether.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/** Mapa de Google con un marcador en la ubicación del evento. */
@Composable
fun MapaEvento(
    latitud: Double,
    longitud: Double,
    titulo: String,
    referencia: String = ""
) {
    val context = LocalContext.current
    val posicion = LatLng(latitud, longitud)

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicion, 15f)
    }
    val marcador = remember(posicion) { MarkerState(position = posicion) }

    Column {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp)),
            cameraPositionState = camara,
            uiSettings = MapUiSettings(zoomControlsEnabled = true, mapToolbarEnabled = false)
        ) {
            Marker(state = marcador, title = titulo, snippet = referencia)
        }

        // Abre la app de Google Maps para ver cómo llegar
        TextButton(onClick = {
            val uri = Uri.parse("geo:$latitud,$longitud?q=$latitud,$longitud(${Uri.encode(titulo)})")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }) {
            Icon(Icons.Filled.LocationOn, contentDescription = null)
            Text("Cómo llegar")
        }
    }
}
