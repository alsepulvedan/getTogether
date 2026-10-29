package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gettogether.viewmodel.UsuarioViewModel

@Composable
fun Registro(
    viewModel: UsuarioViewModel,
    onVolver: () -> Unit,
    onRegistrado: () -> Unit
) {
    val form by viewModel.registro.collectAsState()
    val errores by viewModel.registroErrores.collectAsState()
    val mostrar by viewModel.mostrarErroresRegistro.collectAsState()

    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Regístrate", onVolver = onVolver)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CampoTexto(form.nombre, { v -> viewModel.onRegistroChange { it.copy(nombre = v) } },
                "Nombre", error = errores.nombre, mostrarError = mostrar, icono=Icons.Filled.Face  )
            CampoTexto(form.nombreUsuario, { v -> viewModel.onRegistroChange { it.copy(nombreUsuario = v) } },
                "Nombre de usuario", error = errores.nombreUsuario, mostrarError = mostrar ,icono=Icons.Filled.AccountBox)
            CampoTexto(form.correo, { v -> viewModel.onRegistroChange { it.copy(correo = v) } },
                "Correo", error = errores.correo, mostrarError = mostrar, tipoTeclado = KeyboardType.Email, icono=Icons.Filled.Email)
            CampoTexto(form.edad, { v -> viewModel.onRegistroChange { it.copy(edad = v.filter(Char::isDigit).take(2)) } },
                "Edad", error = errores.edad, mostrarError = mostrar, tipoTeclado = KeyboardType.Number, icono=Icons.Filled.DateRange)
            CampoTexto(form.password, { v -> viewModel.onRegistroChange { it.copy(password = v) } },
                "Contraseña", error = errores.password, mostrarError = mostrar,
                tipoTeclado = KeyboardType.Password, esPassword = true, icono=Icons.Filled.Lock)
            CampoTexto(form.confirmar, { v -> viewModel.onRegistroChange { it.copy(confirmar = v) } },
                "Confirmar contraseña", error = errores.confirmar, mostrarError = mostrar,
                tipoTeclado = KeyboardType.Password, esPassword = true, icono=Icons.Filled.Lock)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = form.aceptaTerminos,
                    onCheckedChange = { v -> viewModel.onRegistroChange { it.copy(aceptaTerminos = v) } }
                )
                Text("Acepto los términos y condiciones")
            }
            if (mostrar) TextoError(errores.terminos)

            Spacer(Modifier.height(14.dp))
            Button(
                onClick = { if (viewModel.registrar()) onRegistrado() },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Crear cuenta")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
