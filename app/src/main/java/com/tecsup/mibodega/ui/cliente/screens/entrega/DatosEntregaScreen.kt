package com.tecsup.mibodega.ui.cliente.screens.entrega

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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.DatosEntrega
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.costoDelivery
import com.tecsup.mibodega.ui.cliente.modelo.listaHorarios
import com.tecsup.mibodega.ui.cliente.modelo.listaMetodosPago
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.modelo.subtotal
import com.tecsup.mibodega.ui.cliente.modelo.total
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.componentes.FilaChips
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 7: Datos de entrega (mockup "Cliente").
 *
 * Es "hoja": guarda su propio formulario con `rememberSaveable` (sin ViewModel)
 * y al confirmar entrega un [DatosEntrega] ya validado. Quien coordina
 * (AppNavegacion) guarda esos datos y navega a la confirmación.
 *
 * @param carrito solo para mostrar el resumen del pedido (no lo modifica)
 */
@Composable
fun DatosEntregaScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onConfirmar: (DatosEntrega) -> Unit
) {
    var direccion by rememberSaveable { mutableStateOf("") }
    var referencia by rememberSaveable { mutableStateOf("") }
    var metodoPago by rememberSaveable { mutableStateOf(listaMetodosPago.first()) }
    var horario by rememberSaveable { mutableStateOf(listaHorarios.first()) }

    val completo = direccion.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        ResumenPedido(carrito = carrito)

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Dirección de entrega",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123"
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Método de pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        FilaChips(
            opciones = listaMetodosPago,
            seleccionado = metodoPago,
            onSeleccion = { metodoPago = it }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Horario de entrega",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        FilaChips(
            opciones = listaHorarios,
            seleccionado = horario,
            onSeleccion = { horario = it }
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            habilitado = completo,
            onClick = {
                onConfirmar(
                    DatosEntrega(
                        direccion = direccion.trim(),
                        referencia = referencia.trim(),
                        metodoPago = metodoPago,
                        horario = horario
                    )
                )
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoEntrega(onVolver: () -> Unit) {
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
        Spacer(Modifier.size(48.dp)) // balancea el ancho del ícono de la izquierda
    }
    Text(
        text = "Revisa dónde y cómo quieres recibir tu pedido",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

/** Resumen con los mismos cálculos que el carrito (extensiones del modelo). */
@Composable
private fun ResumenPedido(carrito: List<ItemCarrito>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FilaDato(etiqueta = "Productos", valor = "${carrito.size}")
        FilaDato(etiqueta = "Subtotal", valor = "S/ %.2f".format(carrito.subtotal))
        FilaDato(etiqueta = "Delivery", valor = "S/ %.2f".format(costoDelivery(carrito)))

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "S/ %.2f".format(carrito.total),
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
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(
            carrito = listOf(
                ItemCarrito(listaProductosFake[4], 1),
                ItemCarrito(listaProductosFake[0], 2)
            ),
            onVolver = {},
            onConfirmar = {}
        )
    }
}