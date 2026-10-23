package com.example.gettogether.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gettogether.viewmodel.BuscarViewModel
import com.example.gettogether.viewmodel.CrearEventoViewModel
import com.example.gettogether.viewmodel.EventoViewModel
import com.example.gettogether.viewmodel.UsuarioViewModel

/** Rutas de todas las pantallas del mockup. */
object Rutas {
    const val SPLASH = "splash"
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val AYUDA = "ayuda"

    const val INICIO = "inicio"
    const val BUSCAR = "buscar"
    const val BANDEJA = "bandeja"
    const val CUENTA = "cuenta"
    const val AJUSTES = "ajustes"
    const val CREAR = "crear"

    const val DETALLE = "evento/{id}"
    const val CHAT = "evento/{id}/chat"
    const val ASISTENTES = "evento/{id}/asistentes"
    const val INFO = "evento/{id}/info"

    fun detalle(id: Int) = "evento/$id"
    fun chat(id: Int) = "evento/$id/chat"
    fun asistentes(id: Int) = "evento/$id/asistentes"
    fun info(id: Int) = "evento/$id/info"
}

/** Pantallas que muestran la barra inferior (Inicio / Buscar / Bandeja / Cuenta). */
private val rutasConBarra = setOf(
    Rutas.INICIO, Rutas.BUSCAR, Rutas.BANDEJA, Rutas.CUENTA, Rutas.CREAR,
    Rutas.DETALLE, Rutas.CHAT, Rutas.ASISTENTES, Rutas.INFO
)

private val argumentoId = listOf(navArgument("id") { type = NavType.IntType })

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    // ViewModels compartidos por todas las pantallas (scope de la Activity)
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val eventoViewModel: EventoViewModel = viewModel()
    val crearEventoViewModel: CrearEventoViewModel = viewModel()
    val buscarViewModel: BuscarViewModel = viewModel()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (rutaActual in rutasConBarra) {
                BarraInferior(rutaActual) { ruta -> navController.irAPestana(ruta) }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            // ----- Acceso -----
            composable(Rutas.SPLASH) {
                AnimacionInicio(onTerminar = {
                    navController.navigate(Rutas.BIENVENIDA) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                })
            }
            composable(Rutas.BIENVENIDA) {
                Bienvenida(
                    onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onAyuda = { navController.navigate(Rutas.AYUDA) }
                )
            }
            composable(Rutas.LOGIN) {
                Login(
                    viewModel = usuarioViewModel,
                    onVolver = { navController.popBackStack() },
                    onIrARegistro = {
                        navController.navigate(Rutas.REGISTRO) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                    },
                    onIngresar = { navController.entrarALaApp() }
                )
            }
            composable(Rutas.REGISTRO) {
                Registro(
                    viewModel = usuarioViewModel,
                    onVolver = { navController.popBackStack() },
                    onRegistrado = { navController.entrarALaApp() }
                )
            }
            composable(Rutas.AYUDA) {
                Ayuda(onVolver = { navController.popBackStack() })
            }

            // ----- App -----
            composable(Rutas.INICIO) {
                Inicio(
                    viewModel = eventoViewModel,
                    onAjustes = { navController.navigate(Rutas.AJUSTES) },
                    onCrear = { navController.navigate(Rutas.CREAR) },
                    onVerInfo = { id -> navController.navigate(Rutas.info(id)) }
                )
            }
            composable(Rutas.AJUSTES) {
                AjustesScreen(
                    viewModel = usuarioViewModel,
                    onVolver = { navController.popBackStack() },
                    onAyuda = { navController.navigate(Rutas.AYUDA) },
                    onCerrarSesion = {
                        usuarioViewModel.cerrarSesion()
                        navController.navigate(Rutas.BIENVENIDA) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.CREAR) {
                CrearEvento(
                    viewModel = crearEventoViewModel,
                    onVolver = { navController.popBackStack() },
                    onCreado = { id ->
                        navController.navigate(Rutas.detalle(id)) {
                            popUpTo(Rutas.CREAR) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.BUSCAR) {
                Buscar(
                    viewModel = buscarViewModel,
                    onVolver = { navController.popBackStack() },
                    onVerEvento = { id -> navController.navigate(Rutas.info(id)) }
                )
            }
            composable(Rutas.BANDEJA) {
                Bandeja(
                    viewModel = eventoViewModel,
                    onVolver = { navController.popBackStack() },
                    onVerEvento = { id -> navController.navigate(Rutas.detalle(id)) }
                )
            }
            composable(Rutas.CUENTA) {
                Cuenta(
                    viewModel = usuarioViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }

            // ----- Detalle de evento -----
            composable(Rutas.DETALLE, arguments = argumentoId) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                DetalleEvento(
                    id = id,
                    viewModel = eventoViewModel,
                    onVolver = { navController.popBackStack() },
                    onChat = { navController.navigate(Rutas.chat(id)) },
                    onAsistentes = { navController.navigate(Rutas.asistentes(id)) },
                    onInformacion = { navController.navigate(Rutas.info(id)) }
                )
            }
            composable(Rutas.CHAT, arguments = argumentoId) { entrada ->
                ChatEvento(
                    id = entrada.arguments?.getInt("id") ?: 0,
                    viewModel = eventoViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.ASISTENTES, arguments = argumentoId) { entrada ->
                AsistentesEvento(
                    id = entrada.arguments?.getInt("id") ?: 0,
                    viewModel = eventoViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.INFO, arguments = argumentoId) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                InformacionEvento(
                    id = id,
                    viewModel = eventoViewModel,
                    onVolver = { navController.popBackStack() },
                    onIrAlEvento = {
                        navController.navigate(Rutas.detalle(id)) {
                            popUpTo(Rutas.INFO) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/** Después de login/registro: va a Inicio y borra las pantallas de acceso del historial. */
private fun NavHostController.entrarALaApp() {
    navigate(Rutas.INICIO) {
        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
    }
}

/** Navegación de la barra inferior sin apilar pantallas repetidas. */
private fun NavHostController.irAPestana(ruta: String) {
    navigate(ruta) {
        popUpTo(Rutas.INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
