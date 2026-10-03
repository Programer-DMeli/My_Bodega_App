package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Filtro de opciones: un [LazyRow] de chips con SELECCIÓN ÚNICA.
 * En Inicio son las categorías; en Datos de entrega, el pago y el horario.
 *
 * Es un componente "hoja": no guarda qué opción está activa, la recibe. El mismo
 * control sirve para las categorías de Inicio y para el pago/horario de la
 * entrega, porque quien lo usa decide qué lista le pasa y dónde guarda el valor
 * (en ambas pantallas, con rememberSaveable).
 *
 * Solo se dibujan los chips que entran en pantalla, aunque la lista crezca.
 *
 * @param opciones lista a mostrar (la primera suele ser la que no filtra)
 * @param seleccionada valor activo: solo un chip queda resaltado
 * @param onSeleccion avisa el chip que se tocó
 */
@Composable
fun FilaChips(
    opciones: List<String>,
    seleccionado: String,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(opciones, key = { opcion -> opcion }) { opcion ->
            Chip(
                texto = opcion,
                seleccionado = opcion == seleccionado,
                onClick = { onSeleccion(opcion) }
            )
        }
    }
}

@Composable
private fun Chip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text = texto, color = contenido, fontWeight = FontWeight.Medium)
    }
}