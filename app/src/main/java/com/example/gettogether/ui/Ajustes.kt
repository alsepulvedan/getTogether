package com.example.gettogether.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.UsuarioViewModel

/** Pantalla "AJUSTES" del mockup. */
@Composable
fun AjustesScreen(
    viewModel: UsuarioViewModel,
    onVolver: () -> Unit,
    onAyuda: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val ajustes = viewModel.ajustes

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Ajustes", onVolver = onVolver)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingPantalla)
        ) {
            TituloSeccion("Preferencias")
            FilaSwitch("Notificaciones", "Avisos de nuevos mensajes y eventos", ajustes.notificaciones) { v ->
                viewModel.onAjustesChange { it.copy(notificaciones = v) }
            }
            FilaSwitch("Perfil público", "Otros usuarios pueden ver tu perfil", ajustes.perfilPublico) { v ->
                viewModel.onAjustesChange { it.copy(perfilPublico = v) }
            }
            FilaSwitch("Solo eventos cercanos", "Muestra eventos de tu comuna", ajustes.soloCercanos) { v ->
                viewModel.onAjustesChange { it.copy(soloCercanos = v) }
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            ListItem(
                headlineContent = { Text("Ayuda") },
                supportingContent = { Text("Preguntas frecuentes") },
                leadingContent = { Icon(Icons.Filled.Info, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(0.dp),
                trailingContent = { Button(onClick = onAyuda) { Text("Ver") } }
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión")
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Get Together v1.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FilaSwitch(titulo: String, descripcion: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(titulo) },
        supportingContent = { Text(descripcion) },
        trailingContent = { Switch(checked = activo, onCheckedChange = onCambio) }
    )
}
