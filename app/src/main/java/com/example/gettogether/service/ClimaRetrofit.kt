package com.example.gettogether.service



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit SOLO para la API externa.
 */
object ClimaRetrofit {
    private const val URL_BASE = "https://api.open-meteo.com/"

    val api: ClimaApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApi::class.java)
    }
}