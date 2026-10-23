package com.example.gettogether.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gettogether.model.Evento
import com.example.gettogether.model.FiltrosBusqueda
import com.example.gettogether.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BuscarViewModel : ViewModel() {

    private val _filtros = MutableStateFlow(FiltrosBusqueda())
    val filtros: StateFlow<FiltrosBusqueda> = _filtros

    fun actualizar(cambio: (FiltrosBusqueda) -> FiltrosBusqueda) {
        _filtros.value = cambio(_filtros.value)
    }

    fun limpiarFiltros() {
        // Mantiene el texto del buscador, limpia solo los filtros
        _filtros.value = FiltrosBusqueda(texto = _filtros.value.texto)
    }

    fun resultados(filtros: FiltrosBusqueda): List<Evento> = EventoRepository.buscar(filtros)
}
