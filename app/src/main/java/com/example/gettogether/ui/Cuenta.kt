package com.example.gettogether.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.UsuarioViewModel

/**
 * Pantalla "Cuenta" del mockup: Nombre + Foto, nombre usuario, correo, ubicación,
 * intereses, edad y número de teléfono.
 */
@Composable
fun Cuenta(viewModel: UsuarioViewModel, onVolver: () -> Unit) {
    val perfil by viewModel.perfil.collectAsState()
    var mensaje by remember { mutableStateOf<String?>(null) }
    var esError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.cargarPerfil() }

    val selectorFoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.onPerfilChange { it.copy(foto = uri.toString()) }
    }

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Cuenta", onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingPantalla),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Nombre", style = MaterialTheme.typography.labelLarge)
                    Text(
                        perfil.nombre.ifBlank { "Sin nombre" },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("@${perfil.nombreUsuario}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // Foto / Fotos
                OutlinedCard(
                    onClick = {
                        selectorFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.size(width = 140.dp, height = 100.dp)
                ) {
                    val foto = perfil.foto
                    if (foto != null) {
                        ImagenUri(foto, Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)), "Foto de perfil")
                    } else {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null)
                            Text("Agregar foto", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            CampoTexto(perfil.nombre, { v -> viewModel.onPerfilChange { it.copy(nombre = v) } }, "Nombre")
            // El nombre de usuario identifica al usuario en los eventos, por eso no se edita
            CampoTexto(perfil.nombreUsuario, {}, "Nombre usuario", habilitado = false)
            CampoTexto(perfil.correo, { v -> viewModel.onPerfilChange { it.copy(correo = v) } }, "Correo",
                tipoTeclado = KeyboardType.Email)
            CampoTexto(perfil.ubicacion, { v -> viewModel.onPerfilChange { it.copy(ubicacion = v) } }, "Ubicación")
            CampoTexto(perfil.intereses, { v -> viewModel.onPerfilChange { it.copy(intereses = v) } }, "Intereses")
            CampoTexto(
                perfil.edad?.toString().orEmpty(),
                { v -> viewModel.onPerfilChange { it.copy(edad = v.filter(Char::isDigit).take(2).toIntOrNull()) } },
                "Edad",
                tipoTeclado = KeyboardType.Number
            )
            CampoTexto(perfil.telefono, { v -> viewModel.onPerfilChange { it.copy(telefono = v) } },
                "Número de teléfono", tipoTeclado = KeyboardType.Phone)

            mensaje?.let {
                Text(it, color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val error = viewModel.guardarPerfil()
                    esError = error != null
                    mensaje = error ?: "Perfil actualizado"
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Guardar cambios")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
