package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.tecsup.mibodega.ui.theme.AzulEnlace

/**
 * Texto azul tipo enlace. Se usa en: Bienvenida ("Términos y
 * Condiciones") y en Login ("Crear cuenta").
 *
 * Es un componente tonto: no navega, solo avisa el click.
 */
@Composable
fun EnlaceAzul(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = AzulEnlace,
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clickable(onClick = onClick)
    )
}