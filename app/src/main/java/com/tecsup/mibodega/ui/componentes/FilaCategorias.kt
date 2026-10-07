package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Componente [LazyRow] para mostrar la lista de categorías con selección única.
 *
 * Cada chip resalta según la categoría activa. Al seleccionar una categoría diferente,
 * se notifica mediante [onCategoriaSeleccionada] para filtrar la lista principal de productos.
 *
 * @param categorias Lista de nombres de categorías a mostrar
 * @param categoriaSeleccionada Categoría activa actualmente
 * @param onCategoriaSeleccionada Callback cuando el usuario selecciona una categoría
 */
@Composable
fun FilaCategorias(
    categorias: List<String>,
    categoriaSeleccionada: String,
    onCategoriaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(categorias, key = { it }) { categoria ->
            val esSeleccionado = categoria == categoriaSeleccionada
            val fondo = if (esSeleccionado) VerdeBodega else GrisClaro
            val colorTexto = if (esSeleccionado) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(fondo)
                    .clickable { onCategoriaSeleccionada(categoria) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = categoria,
                    color = colorTexto,
                    fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
