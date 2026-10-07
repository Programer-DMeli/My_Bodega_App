package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.componentes.EnlaceAzul
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Pantalla 2: Iniciar sesión.
 *
 * @param onVolver vuelve a la pantalla de bienvenida
 * @param onIniciarSesion recibe (telefono, clave) si el formulario es válido
 * @param onCrearCuenta lleva al formulario de registro
 */
@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onIniciarSesion: (telefono: String, clave: String) -> Unit,
    onCrearCuenta: () -> Unit
) {
    // ---- Estado local del formulario (rememberSaveable sobrevive a rotación) ----
    var telefono by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var mensajeError by rememberSaveable { mutableStateOf("") }

    // ---- Validación: se recalcula sola en cada recomposición ----
    val digitos = telefono.filter { it.isDigit() }.length
    val formularioCompleto = digitos == CANTIDAD_DIGITOS_CELULAR && clave.isNotEmpty()

    fun validar(): String? = when {
        digitos != CANTIDAD_DIGITOS_CELULAR -> "Ingresa un celular de $CANTIDAD_DIGITOS_CELULAR dígitos"
        clave.length < LONGITUD_MINIMA_CLAVE -> "La contraseña debe tener al menos $LONGITUD_MINIMA_CLAVE caracteres"
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Ingresa tu número de celular y contraseña para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = {
                telefono = it
                mensajeError = "Ingresa tu Telefono valido"
            },
            teclado = KeyboardType.Phone,
            icono = rememberVectorPainter(Icons.Default.Phone)
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = clave,
            onValorCambia = {
                clave = it
                mensajeError = ""
            },
            icono = rememberVectorPainter(Icons.Default.Lock),
            esContrasena = true
        )

        // El mensaje de error solo ocupa espacio cuando hay algo que avisar.
        if (mensajeError.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensajeError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Iniciar sesión",
            habilitado = formularioCompleto,
            onClick = {
                val error = validar()
                if (error == null) {
                    onIniciarSesion(telefono.trim(), clave)
                } else {
                    mensajeError = error
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        EnlaceCrearCuenta(onCrearCuenta = onCrearCuenta)

        Spacer(Modifier.height(24.dp))
    }
}

// Constantes de validación de la app (celular peruano de 9 dígitos).
private const val CANTIDAD_DIGITOS_CELULAR = 9
private const val LONGITUD_MINIMA_CLAVE = 6

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoLogin(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp)) // balancea el ancho del ícono de la izquierda
    }
}

@Composable
private fun EnlaceCrearCuenta(onCrearCuenta: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "¿No tienes cuenta?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(5.dp))
        EnlaceAzul(
            texto = "Crear cuenta",
            onClick = onCrearCuenta
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginPreview() {
    BodegaTheme {
        LoginScreen(
            onVolver = {},
            onIniciarSesion = { _, _ -> },
            onCrearCuenta = {}
        )
    }
}