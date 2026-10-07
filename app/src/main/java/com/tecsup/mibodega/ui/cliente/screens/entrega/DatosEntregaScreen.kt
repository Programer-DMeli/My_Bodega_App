package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: (esDelivery: Boolean) -> Unit
) {
    var direccion by rememberSaveable { mutableStateOf("") }
    var referencia by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var esDelivery by rememberSaveable { mutableStateOf(true) }
    var mensajeError by rememberSaveable { mutableStateOf("") }

    val formularioValido = direccion.isNotBlank() && telefono.length >= 9

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
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
                text = "Datos de entrega",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.size(48.dp))
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Selecciona el método de entrega",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { esDelivery = true }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = esDelivery,
                onClick = { esDelivery = true }
            )
            Text(
                text = "Delivery a domicilio (S/ 4.00)",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { esDelivery = false }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !esDelivery,
                onClick = { esDelivery = false }
            )
            Text(
                text = "Recojo en tienda (Gratis)",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(20.dp))

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = {
                direccion = it
                mensajeError = ""
            },
            placeholder = "Av. Los Olivos 123"
        )
        if (direccion.isBlank() && mensajeError.isNotEmpty()) {
            Text(
                text = "La dirección es obligatoria",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono de contacto",
            valor = telefono,
            onValorCambia = {
                telefono = it
                mensajeError = ""
            },
            teclado = KeyboardType.Phone,
            icono = rememberVectorPainter(Icons.Default.Phone),
            placeholder = "987654321"
        )
        if (telefono.length < 9 && mensajeError.isNotEmpty()) {
            Text(
                text = "Ingresa un teléfono válido de 9 dígitos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

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
            texto = "Confirmar y pagar",
            habilitado = formularioValido,
            onClick = {
                if (formularioValido) {
                    onConfirmarPedido(esDelivery)
                } else {
                    mensajeError = "Por favor completa los campos obligatorios"
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(onVolver = {}, onConfirmarPedido = {})
    }
}
