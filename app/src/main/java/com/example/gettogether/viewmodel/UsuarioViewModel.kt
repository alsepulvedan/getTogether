package com.example.gettogether.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gettogether.model.Ajustes
import com.example.gettogether.model.LoginForm
import com.example.gettogether.model.RegistroForm
import com.example.gettogether.model.RegistroFormError
import com.example.gettogether.model.Usuario
import com.example.gettogether.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Lógica de Login, Registro, Cuenta y Ajustes. */
class UsuarioViewModel : ViewModel() {

    private val regexCorreo = Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")

    val usuarioActual: Usuario? get() = UsuarioRepository.usuarioActual
    val ajustes: Ajustes get() = UsuarioRepository.ajustes

    // ---------- Login ----------
    private val _login = MutableStateFlow(LoginForm())
    val login: StateFlow<LoginForm> = _login

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun onLoginCorreoChange(valor: String) { _login.value = _login.value.copy(correo = valor) }
    fun onLoginPasswordChange(valor: String) { _login.value = _login.value.copy(password = valor) }

    fun iniciarSesion(): Boolean {
        val f = _login.value
        if (f.correo.isBlank() || f.password.isBlank()) {
            _loginError.value = "Completa correo y contraseña"
            return false
        }
        return if (UsuarioRepository.login(f.correo, f.password)) {
            _loginError.value = null
            _login.value = LoginForm()
            true
        } else {
            _loginError.value = "Correo o contraseña incorrectos"
            false
        }
    }

    // ---------- Registro ----------
    private val _registro = MutableStateFlow(RegistroForm())
    val registro: StateFlow<RegistroForm> = _registro

    private val _registroErrores = MutableStateFlow(validarRegistro(RegistroForm()))
    val registroErrores: StateFlow<RegistroFormError> = _registroErrores

    private val _mostrarErroresRegistro = MutableStateFlow(false)
    val mostrarErroresRegistro: StateFlow<Boolean> = _mostrarErroresRegistro

    fun onRegistroChange(cambio: (RegistroForm) -> RegistroForm) {
        _registro.value = cambio(_registro.value)
        _registroErrores.value = validarRegistro(_registro.value)
    }

    fun registrar(): Boolean {
        val errores = validarRegistro(_registro.value)
        _registroErrores.value = errores
        _mostrarErroresRegistro.value = true
        if (!errores.esValido) return false

        val f = _registro.value
        UsuarioRepository.registrar(
            Usuario(
                nombre = f.nombre.trim(),
                nombreUsuario = f.nombreUsuario.trim(),
                correo = f.correo.trim(),
                password = f.password,
                edad = f.edad.toIntOrNull()
            )
        )
        _registro.value = RegistroForm()
        _mostrarErroresRegistro.value = false
        return true
    }

    private fun validarRegistro(f: RegistroForm) = RegistroFormError(
        nombre = if (f.nombre.isBlank()) "El nombre es obligatorio" else null,
        nombreUsuario = when {
            f.nombreUsuario.isBlank() -> "Elige un nombre de usuario"
            f.nombreUsuario.contains(" ") -> "No puede tener espacios"
            UsuarioRepository.existeNombreUsuario(f.nombreUsuario) -> "Ese nombre de usuario ya existe"
            else -> null
        },
        correo = when {
            !f.correo.trim().matches(regexCorreo) -> "Correo inválido"
            UsuarioRepository.existeCorreo(f.correo) -> "Ese correo ya está registrado"
            else -> null
        },
        edad = f.edad.toIntOrNull().let { if (it == null || it !in 18..99) "Edad entre 18 y 99" else null },
        password = if (f.password.length < 6) "Mínimo 6 caracteres" else null,
        confirmar = if (f.confirmar != f.password) "Las contraseñas no coinciden" else null,
        terminos = if (!f.aceptaTerminos) "Debes aceptar los términos" else null
    )

    // ---------- Cuenta (perfil) ----------
    private val _perfil = MutableStateFlow(Usuario())
    val perfil: StateFlow<Usuario> = _perfil

    fun cargarPerfil() {
        _perfil.value = UsuarioRepository.usuarioActual ?: Usuario()
    }

    fun onPerfilChange(cambio: (Usuario) -> Usuario) {
        _perfil.value = cambio(_perfil.value)
    }

    /** Devuelve un mensaje de error, o null si se guardó correctamente. */
    fun guardarPerfil(): String? {
        val p = _perfil.value
        val error = when {
            UsuarioRepository.usuarioActual == null -> "Debes iniciar sesión"
            p.nombre.isBlank() -> "El nombre es obligatorio"
            !p.correo.trim().matches(regexCorreo) -> "Correo inválido"
            p.edad != null && p.edad !in 18..99 -> "Edad entre 18 y 99"
            else -> null
        }
        if (error == null) UsuarioRepository.actualizarPerfil(p)
        return error
    }

    // ---------- Ajustes ----------
    fun onAjustesChange(cambio: (Ajustes) -> Ajustes) {
        UsuarioRepository.actualizarAjustes(cambio(UsuarioRepository.ajustes))
    }

    fun cerrarSesion() = UsuarioRepository.cerrarSesion()
}
