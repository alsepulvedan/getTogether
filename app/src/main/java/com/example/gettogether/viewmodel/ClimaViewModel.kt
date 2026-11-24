package com.example.gettogether.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gettogether.model.ClimaEstado
import com.example.gettogether.repository.ClimaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Lógica de la tarjeta de clima en Información del evento. */
class ClimaViewModel(
    private val repository: ClimaRepository = ClimaRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<ClimaEstado>(ClimaEstado.Cargando)
    val estado: StateFlow<ClimaEstado> = _estado

    fun cargar(latitud: Double, longitud: Double) {
        _estado.value = ClimaEstado.Cargando
        viewModelScope.launch {
            _estado.value = repository.obtenerClima(latitud, longitud).fold(
                onSuccess = { ClimaEstado.Exito(it) },
                onFailure = { ClimaEstado.Error("No se pudo obtener el clima") }
            )
        }
    }
}