package com.example.gettogether.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Pantalla 1 del mockup: "Animación Inicio". Al terminar pasa a Bienvenida. */
@Composable
fun AnimacionInicio(onTerminar: () -> Unit) {
    val escala = remember { Animatable(0.3f) }
    val opacidad = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { opacidad.animateTo(1f, animationSpec = tween(durationMillis = 900)) }
        escala.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        delay(900)
        onTerminar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.graphicsLayer {
                scaleX = escala.value
                scaleY = escala.value
                alpha = opacidad.value
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoGetTogether(Modifier.size(140.dp), color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.height(16.dp))
            Text(
                "Get Together",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "Panoramas con gente como tú",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}
