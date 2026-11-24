package com.example.gettogether.model

import com.google.gson.annotations.SerializedName

// ---------- Respuesta de la API externa Open-Meteo ----------


data class ClimaRespuesta(
    @SerializedName("current") val actual: ClimaActual?
)

data class ClimaActual(
    @SerializedName("temperature_2m") val temperatura: Double,
    @SerializedName("weather_code") val codigo: Int,
    @SerializedName("wind_speed_10m") val viento: Double
)

// ---------- Modelo que usa la app ----------

data class Clima(
    val temperatura: Double,
    val viento: Double,
    val descripcion: String
)

/** Estados de la tarjeta de clima en la interfaz. */
sealed interface ClimaEstado {
    data object Cargando : ClimaEstado
    data class Exito(val clima: Clima) : ClimaEstado
    data class Error(val mensaje: String) : ClimaEstado
}