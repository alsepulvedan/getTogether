package com.example.gettogether.repository


import com.example.gettogether.model.Clima
import com.example.gettogether.service.ClimaApi
import com.example.gettogether.service.ClimaRetrofit
import kotlin.coroutines.cancellation.CancellationException

/** Obtiene el clima actual desde Open-Meteo. Recibe la API por constructor para poder testearlo con MockK. */
class ClimaRepository(private val api: ClimaApi = ClimaRetrofit.api) {

    suspend fun obtenerClima(latitud: Double, longitud: Double): Result<Clima> {
        return try {
            val actual = api.obtenerClima(latitud, longitud).actual
                ?: return Result.failure(IllegalStateException("La respuesta no trae datos de clima"))
            Result.success(
                Clima(
                    temperatura = actual.temperatura,
                    viento = actual.viento,
                    descripcion = describirCodigo(actual.codigo)
                )
            )
        } catch (e: CancellationException) {
            throw e // si se cierra la pantalla, se cancela normalmente
        } catch (e: Exception) {
            Result.failure(e) // sin internet, timeout, error del servidor, etc.
        }
    }

    companion object {
        /** Traduce el código WMO que entrega Open-Meteo a un texto en español. */
        fun describirCodigo(codigo: Int): String = when (codigo) {
            0 -> "Despejado"
            1, 2 -> "Parcialmente nublado"
            3 -> "Nublado"
            45, 48 -> "Neblina"
            51, 53, 55, 56, 57 -> "Llovizna"
            61, 63, 65, 66, 67, 80, 81, 82 -> "Lluvia"
            71, 73, 75, 77, 85, 86 -> "Nieve"
            95, 96, 99 -> "Tormenta"
            else -> "Sin información"
        }
    }
}