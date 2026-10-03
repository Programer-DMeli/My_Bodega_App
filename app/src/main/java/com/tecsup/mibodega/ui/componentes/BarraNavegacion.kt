package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.mibodega.navegacion.Rutas
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Una pestaña de la barra inferior: ruta de destino + cómo se ve.
 */
data class Pestana(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

/** Las pestañas principales de la app, en el orden en que se muestran. */
val PESTANAS = listOf(
    Pestana(Rutas.INICIO, "Inicio", Icons.Default.Home),
    Pestana(Rutas.CATEGORIAS, "Categorías", Icons.AutoMirrored.Filled.List),
    Pestana(Rutas.PEDIDOS, "Pedidos", Icons.Default.Receipt),
    Pestana(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

/**
 * Barra de navegación inferior (NavigationBar del Material 3).
 *
 * Es un componente "hoja" como el resto: no sabe navegar, solo informa.
 * - [destinoActual] es la ruta donde está el usuario: define qué pestaña
 *   aparece seleccionada.
 * - [onDestinoSeleccionado] avisa qué pestaña se tocó.
 *
 * Se usa en el `bottomBar` del Scaffold principal (ver AppNavegacion), que
 * envuelve al NavHost para que la barra siga visible al cambiar de pestaña.
 */
@Composable
fun BarraNavegacion(
    destinoActual: String,
    onDestinoSeleccionado: (String) -> Unit
) {
    NavigationBar(modifier = Modifier) {
        PESTANAS.forEach { pestana ->
            NavigationBarItem(
                selected = destinoActual == pestana.ruta,
                onClick = { onDestinoSeleccionado(pestana.ruta) },
                icon = { Icon(pestana.icono, contentDescription = null) },
                label = { Text(pestana.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega,
                    indicatorColor = GrisClaro
                )
            )
        }
    }
}