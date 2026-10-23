package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.UsuarioViewModel

@Composable
fun Login(
    viewModel: UsuarioViewModel,
    onVolver: () -> Unit,
    onIrARegistro: () -> Unit,
    onIngresar: () -> Unit
) {
    val form by viewModel.login.collectAsState()
    val error by viewModel.loginError.collectAsState()

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Iniciar sesión", onVolver = onVolver)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoTexto(
                valor = form.correo,
                onCambio = viewModel::onLoginCorreoChange,
                etiqueta = "Correo",
                tipoTeclado = KeyboardType.Email,
                icono = Icons.Filled.Email
            )
            CampoTexto(
                valor = form.password,
                onCambio = viewModel::onLoginPasswordChange,
                etiqueta = "Contraseña",
                tipoTeclado = KeyboardType.Password,
                esPassword = true,
                icono = Icons.Filled.Lock
            )
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { if (viewModel.iniciarSesion()) onIngresar() },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Entrar")
            }
            TextButton(onClick = onIrARegistro) { Text("¿No tienes cuenta? Regístrate") }

            Spacer(Modifier.height(24.dp))
            Text(
                "Cuenta de prueba: demo@gettogether.cl / 123456",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
