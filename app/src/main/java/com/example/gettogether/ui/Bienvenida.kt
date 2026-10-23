package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Pantalla 2 del mockup: logo "Get Together" + Iniciar sesión / Regístrate / Ayuda. */
@Composable
fun Bienvenida(
    onIniciarSesion: () -> Unit,
    onRegistrarse: () -> Unit,
    onAyuda: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoGetTogether(Modifier.size(110.dp))
                Spacer(Modifier.height(12.dp))
                Text(
                    "Get Together",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Encuentra y crea panoramas cerca de ti",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(48.dp))

        Button(onClick = onIniciarSesion, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Iniciar sesión")
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onRegistrarse, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Regístrate")
        }
        Spacer(Modifier.height(48.dp))
        TextButton(onClick = onAyuda, modifier = Modifier.fillMaxWidth()) {
            Text("Ayuda")
        }
    }
}
