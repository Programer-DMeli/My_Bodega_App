package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Input con label arriba (fuera del recuadro), como en los mockups
 * de Registro, Login y Datos de entrega.
 *
 * Es un componente tonto: no sabe de usuarios ni de validación,
 * solo muestra el texto y avisa cada cambio.
 *
 * @param teclado tipo de teclado, ej. KeyboardType.Phone para el teléfono
 * @param icono ícono opcional a la izquierda del campo
 * @param esContrasena si es true, oculta el texto y muestra el botón de ver/ocultar
 */
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    icono: Painter? = null,
    esContrasena: Boolean = false
) {

    // rememberSaveable para que no se pierda al rotar la pantalla.
    var claveVisible by rememberSaveable { mutableStateOf(false) }

    val transformacion = if (esContrasena && !claveVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            leadingIcon = icono?.let { { Icon(painter = it, contentDescription = null) } },
            trailingIcon = if (esContrasena) {
                {
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            imageVector = if (claveVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (claveVisible) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            }
                        )
                    }
                }
            } else {
                null
            },
            visualTransformation = transformacion,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esContrasena && !claveVisible) KeyboardType.Password else teclado
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}