package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.DatosEntrega
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 8: Confirmación del pedido (mockup "Cliente").
 *
 * Es el cierre del flujo: ya no se edita nada, solo muestra el resumen y
 * devuelve al inicio. No hay flecha de "atrás" a propósito, porque en la
 * navegación se limpió el historial con popUpTo (ver AppNavegacion).
 *
 * @param codigoPedido código generado al confirmar (ej. "MB-4821")
 * @param total importe pagado, ya calculado al confirmar el pedido
 */
@Composable
fun ConfirmacionScreen(
    codigoPedido: String,
    datosEntrega: DatosEntrega,
    total: Double,
    onVolverAlInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        IconoExito()

        Spacer(Modifier.height(20.dp))

        Text(
            text = "¡Pedido confirmado!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Tu código de pedido es $codigoPedido",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        ResumenEntrega(datosEntrega = datosEntrega, total = total)

        Spacer(Modifier.height(32.dp))

        BotonPrimario(
            texto = "Seguir comprando",
            onClick = onVolverAlInicio
        )

        Spacer(Modifier.height(12.dp))

        BotonSecundario(
            texto = "Ver mis pedidos",
            onClick = onVolverAlInicio
        )

        Spacer(Modifier.height(32.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun IconoExito() {
    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .size(96.dp)
            .background(VerdeBodega, CircleShape)
            .padding(24.dp)
    )
}

@Composable
private fun ResumenEntrega(datosEntrega: DatosEntrega, total: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Resumen del pedido",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        FilaDato(etiqueta = "Dirección", valor = datosEntrega.direccion)

        if (datosEntrega.referencia.isNotBlank()) {
            FilaDato(etiqueta = "Referencia", valor = datosEntrega.referencia)
        }

        FilaDato(etiqueta = "Pago", valor = datosEntrega.metodoPago)
        FilaDato(etiqueta = "Entrega", valor = datosEntrega.horario)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total pagado", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleMedium,
                color = VerdeBodega
            )
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = valor, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfirmacionPreview() {
    BodegaTheme {
        ConfirmacionScreen(
            codigoPedido = "MB-4821",
            datosEntrega = DatosEntrega(
                direccion = "Av. Los Olivos 123",
                referencia = "Frente al parque",
                metodoPago = "Tarjeta",
                horario = "Hoy"
            ),
            total = 10.50,
            onVolverAlInicio = {}
        )
    }
}