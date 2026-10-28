package com.example.gettogether.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gettogether.model.Evento
import com.example.gettogether.ui.theme.VerdeEvento
import com.example.gettogether.ui.theme.VerdeEventoTexto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ---------------------------------------------------------------------------
// Barras
// ---------------------------------------------------------------------------

/** Barra superior. Si [onVolver] no es null muestra el botón "Volver" del mockup. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    iconoIzquierdo: (@Composable () -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            when {
                iconoIzquierdo != null -> iconoIzquierdo()
                onVolver != null -> IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = acciones
    )
}

private data class ItemBarra(val ruta: String, val titulo: String, val icono: ImageVector)

private val itemsBarra = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.BUSCAR, "Buscar", Icons.Filled.Search),
    ItemBarra(Rutas.BANDEJA, "Bandeja", Icons.Filled.Email),
    ItemBarra(Rutas.CUENTA, "Cuenta", Icons.Filled.AccountCircle)
)

/** Barra inferior: Inicio / Buscar / Bandeja / Cuenta. */
@Composable
fun BarraInferior(rutaActual: String?, onNavegar: (String) -> Unit) {
    NavigationBar {
        itemsBarra.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { onNavegar(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.titulo) },
                label = { Text(item.titulo) }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Formularios
// ---------------------------------------------------------------------------

/** OutlinedTextField con el mensaje de error abajo (patrón de UsuarioFormScreen en basic-app). */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    mostrarError: Boolean = true,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esPassword: Boolean = false,
    lineasMinimas: Int = 1,
    habilitado: Boolean = true,
    icono: ImageVector? = null
) {
    val hayError = mostrarError && error != null
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        isError = hayError,
        enabled = habilitado,
        singleLine = lineasMinimas == 1,
        minLines = lineasMinimas,
        leadingIcon = if (icono != null) {
            { Icon(icono, contentDescription = null) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = if (hayError) {
            { Text(error.orEmpty(), color = MaterialTheme.colorScheme.error) }
        } else null
    )
}

@Composable
fun TextoError(mensaje: String?) {
    if (mensaje != null) {
        Text(
            mensaje,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Composable
fun TituloSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

// ---------------------------------------------------------------------------
// Eventos
// ---------------------------------------------------------------------------

@Composable
fun FilaEtiquetas(etiquetas: List<String>, modifier: Modifier = Modifier) {
    if (etiquetas.isEmpty()) return
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(etiquetas) { etiqueta ->
            SuggestionChip(onClick = {}, label = { Text("#$etiqueta") })
        }
    }
}

/** Cuadro de imagen genérico cuando el evento no tiene fotos. */
@Composable
fun PlaceholderImagen(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VerdeEventoTexto.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = VerdeEventoTexto.copy(alpha = 0.6f)
        )
    }
}

/** Primera imagen del evento o un placeholder con el tipo. */
@Composable
fun PortadaEvento(evento: Evento, modifier: Modifier = Modifier) {
    val primeraRes = evento.portadaRes ?: evento.imagenesRes.firstOrNull()
    val primera = evento.imagenes.firstOrNull()
    if (primeraRes != null) {
        Image(
            painter = painterResource(primeraRes),
            contentDescription = evento.nombre,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(16.dp))
        )
    } else if (primera != null) {
        ImagenUri(primera, modifier = modifier.clip(RoundedCornerShape(16.dp)), descripcion = evento.nombre)
    } else {
        PlaceholderImagen(evento.tipo, modifier)
    }
}

/** Tarjeta corta de evento (Bandeja / resultados de Buscar). */
@Composable
fun EventoResumenCard(evento: Evento, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = VerdeEvento, contentColor = VerdeEventoTexto)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PortadaEvento(evento, Modifier.size(64.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(evento.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text(
                        listOf(evento.ubicacion, evento.referencia).filter { it.isNotBlank() }.joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    "${evento.tipo} · ${evento.asistentes.size}/${evento.cupo} asistentes · ${evento.rangoEtario}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/** Encabezado "Evento Confirmado" que aparece arriba en Chat / Asistentes / Información. */
@Composable
fun EncabezadoEvento(evento: Evento, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = VerdeEvento, contentColor = VerdeEventoTexto)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PortadaEvento(evento, Modifier.size(56.dp))
            Column {
                Text(evento.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("${evento.ubicacion} · ${evento.tipo}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Círculo con la inicial del usuario. */
@Composable
fun Avatar(nombre: String, tamano: Dp = 40.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (nombre.isBlank()) {
            Icon(Icons.Filled.Person, contentDescription = null)
        } else {
            Text(
                nombre.first().uppercase(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Logo
// ---------------------------------------------------------------------------

/** Logo simple: tres círculos que se cruzan (gente que se junta). */
@Composable
fun LogoGetTogether(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Canvas(modifier = modifier) {
        val r = size.minDimension / 3.2f
        val centro = Offset(size.width / 2, size.height / 2)
        val d = r * 0.75f
        drawCircle(color.copy(alpha = 0.55f), r, Offset(centro.x - d, centro.y + d * 0.55f))
        drawCircle(color.copy(alpha = 0.55f), r, Offset(centro.x + d, centro.y + d * 0.55f))
        drawCircle(color.copy(alpha = 0.75f), r, Offset(centro.x, centro.y - d * 0.65f))
    }
}

// ---------------------------------------------------------------------------
// Imágenes desde Uri (sin librerías externas)
// ---------------------------------------------------------------------------

@Composable
fun ImagenUri(uri: String, modifier: Modifier = Modifier, descripcion: String? = null) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(initialValue = null, uri) {
        value = withContext(Dispatchers.IO) { cargarBitmap(context, Uri.parse(uri)) }
    }
    val imagen = bitmap
    if (imagen != null) {
        Image(bitmap = imagen, contentDescription = descripcion, modifier = modifier, contentScale = ContentScale.Crop)
    } else {
        Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
    }
}

/** Carga la imagen reducida para no usar demasiada memoria. */
private fun cargarBitmap(context: Context, uri: Uri, ladoMaximo: Int = 800): ImageBitmap? = try {
    val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, limites) }
    var muestra = 1
    while (limites.outWidth / muestra > ladoMaximo || limites.outHeight / muestra > ladoMaximo) muestra *= 2
    val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
    context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
        ?.asImageBitmap()
} catch (e: Exception) {
    null
}

val PaddingPantalla = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
