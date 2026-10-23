# GetTogether

App Android en **Kotlin + Jetpack Compose** con arquitectura **MVVM**, construida a partir del mockup
`Get_together.drawio` y siguiendo la estructura de `basic-app` (Duoc).

## Estructura

```
com.example.gettogether
├── MainActivity.kt          → GetTogetherTheme { Navegacion() }
├── model/                   → data classes (Evento, Usuario, Mensaje, formularios y filtros)
├── repository/              → EventoRepository y UsuarioRepository (datos en memoria con mutableStateOf)
├── viewmodel/               → EventoViewModel, CrearEventoViewModel, BuscarViewModel, UsuarioViewModel
└── ui/                      → pantallas Compose + Navegacion.kt + Componentes.kt
    └── theme/               → colores (verde del mockup), tema y tipografía
```

## Pantallas del mockup → archivo

| Mockup | Archivo | Ruta |
|---|---|---|
| Animación Inicio | `ui/AnimacionInicio.kt` | `splash` |
| Get Together / Iniciar sesión / Regístrate / Ayuda | `ui/Bienvenida.kt`, `Login.kt`, `Registro.kt`, `Ayuda.kt` | `bienvenida`, `login`, `registro`, `ayuda` |
| Evento (Ajustes, Crear, barra inferior) | `ui/Inicio.kt` | `inicio` |
| AJUSTES | `ui/Ajustes.kt` | `ajustes` |
| Crear Evento | `ui/CrearEvento.kt` | `crear` |
| Buscador + Filtros | `ui/Buscar.kt` | `buscar` |
| Bandeja (eventos confirmados) | `ui/Bandeja.kt` | `bandeja` |
| Cuenta | `ui/Cuenta.kt` | `cuenta` |
| Evento confirmado (Chat / Asistentes / Información) | `ui/DetalleEvento.kt` | `evento/{id}` |
| Chat evento | `ui/ChatEvento.kt` | `evento/{id}/chat` |
| Asistentes evento | `ui/AsistentesEvento.kt` | `evento/{id}/asistentes` |
| Información evento | `ui/InformacionEvento.kt` | `evento/{id}/info` |

## Cuenta de prueba

`demo@gettogether.cl` / `123456` (ya tiene 4 eventos confirmados en la Bandeja).

## Dependencias agregadas

- `androidx.navigation:navigation-compose`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.compose.material:material-icons-core`

Las imágenes (Crear evento y foto de Cuenta) usan el **Photo Picker** del sistema, que no necesita permisos.
Los datos viven en memoria: se reinician al cerrar la app.
