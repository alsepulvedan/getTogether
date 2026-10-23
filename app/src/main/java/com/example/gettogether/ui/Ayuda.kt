package com.example.gettogether.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val preguntas = listOf(
    "¿Qué es Get Together?" to "Una app para encontrar personas con tus mismos intereses y juntarse en eventos cercanos.",
    "¿Cómo me uno a un evento?" to "En Inicio desliza entre los eventos y presiona \"¡Me uno!\". El evento quedará en tu Bandeja.",
    "¿Cómo creo un evento?" to "En Inicio presiona el botón Crear (+), completa el formulario y presiona \"Crear evento\".",
    "¿Dónde converso con los asistentes?" to "En Bandeja entra a un evento confirmado y abre el Chat del evento.",
    "¿Cómo filtro eventos?" to "En Buscar presiona Filtros: ubicación, tipo, cantidad de asistentes, rango etario y etiquetas.",
    "¿Cómo edito mi perfil?" to "En Cuenta puedes cambiar tus datos y tu foto, luego presiona \"Guardar cambios\"."
)

@Composable
fun Ayuda(onVolver: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        BarraSuperior("Ayuda", onVolver = onVolver)
        LazyColumn(
            contentPadding = PaddingPantalla,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(preguntas) { (pregunta, respuesta) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(pregunta, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(respuesta, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
