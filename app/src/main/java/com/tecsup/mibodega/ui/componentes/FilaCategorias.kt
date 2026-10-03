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
 * Filtro de categorías: un [LazyRow] de chips con SELECCIÓN ÚNICA.
 *
 * Es un componente "hoja": no guarda qué categoría está activa, la recibe.
 * Así el filtro puede vivir en cualquier pantalla y el estado sigue siendo
 * de quien lo usa (en Inicio, `categoriaSeleccionada` con rememberSaveable).
 *
 * Solo se dibujan los chips que entran en pantalla, aunque la lista de
 * categorías crezca.
 *
 * @param categorias categorías a mostrar (la primera suele ser "Todos")
 * @param categoriaSeleccionada categoría activa: solo un chip queda resaltado
 * @param onCategoriaSeleccionada avisa el chip que se tocó
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
        items(categorias, key = { categoria -> categoria }) { categoria ->
            ChipCategoria(
                texto = categoria,
                seleccionado = categoria == categoriaSeleccionada,
                onClick = { onCategoriaSeleccionada(categoria) }
            )
        }
    }
}

@Composable
private fun ChipCategoria(
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