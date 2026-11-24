package com.example.gettogether.service



import com.example.gettogether.model.ClimaRespuesta
import retrofit2.http.GET
import retrofit2.http.Query

/** Endpoint de la API externa Open-Meteo (no requiere API key). */
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") campos: String = "temperature_2m,weather_code,wind_speed_10m",
        @Query("timezone") zonaHoraria: String = "auto"
    ): ClimaRespuesta
}