package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.CATEGORIA_TODOS
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.filtrarProductos
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.FilaChips
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 4: Inicio / Productos (mockup "Cliente").
 *
 * Aporta la TopAppBar con el badge del carrito y el contenido: buscador,
 * LazyRow de categorías y un [LazyColumn] con la lista de productos.
 * El [LazyColumn] es la clave aquí: solo crea las filas que se están viendo,
 * así puede listar cientos de productos sin coste.
 *
 * La NavigationBar inferior NO vive aquí, sino en el Scaffold principal
 * (AppNavegacion), para que siga visible al cambiar de pestaña.
 *
 * El filtrado por categoría/búsqueda es estado local con `remember` (sin
 * ViewModel) y se recalcula en cada recomposición.
 *
 * @param productos lista completa (fake por ahora, luego vendrá de un Repository)
 * @param cantidadCarrito para el badge del carrito en la topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
// Búsqueda en tiempo real: cada tecla cambia textoBusqueda y recalcula la lista.
// rememberSaveable: la categoría y el texto sobreviven al giro y al cambio de pestaña.
var categoriaSeleccionada by rememberSaveable { mutableStateOf(listaCategorias.first()) }
var textoBusqueda by rememberSaveable { mutableStateOf("") }

// Los dos filtros se combinan con AND dentro de filtrarProductos. remember con
// claves: solo se recorre la lista cuando cambia algún criterio.
val productosFiltrados = remember(productos, categoriaSeleccionada, textoBusqueda) {
    filtrarProductos(
        productos = productos,
        categoriaSeleccionada = categoriaSeleccionada,
        textoBusqueda = textoBusqueda
    )
}

    Scaffold(
        // Insets y NavigationBar los pone el Scaffold principal (AppNavegacion),
        // así esta pantalla solo aporta su TopAppBar.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                // Botón para borrar la búsqueda de un toque.
                trailingIcon = if (textoBusqueda.isNotEmpty()) {
                    {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                        }
                    }
                } else {
                    null
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GrisClaro,
                    focusedContainerColor = GrisClaro,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedBorderColor = VerdeBodega
                )
            )

            // Filtro por categoría (selección única) + buscador: ambos alimentan la
            // misma lista. El contador muestra que el filtro sí está actuando.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (categoriaSeleccionada == CATEGORIA_TODOS) {
                        "Productos destacados"
                    } else {
                        categoriaSeleccionada
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${productosFiltrados.size} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FilaChips(
                opciones = listaCategorias,
                seleccionado = categoriaSeleccionada,
                onSeleccion = { categoriaSeleccionada = it }
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // El key evita recomponer y recargar imágenes al reordenar/filtrar.
                items(productosFiltrados, key = { producto -> producto.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) }
                    )
                }

                // Estado vacío: se muestra como un ítem más de la lista.
                if (productosFiltrados.isEmpty()) {
                    item(key = "lista_vacia") {
                        MensajeSinProductos(
                            categoria = categoriaSeleccionada,
                            busqueda = textoBusqueda
                        )
                    }
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun MensajeSinProductos(categoria: String, busqueda: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = GrisClaro,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(12.dp))
        // Muestra los dos criterios activos para que se vea cuál falló.
        val mensaje = when {
            busqueda.isNotBlank() && categoria != CATEGORIA_TODOS ->
                "Sin resultados para \"$busqueda\" en \"$categoria\""
            busqueda.isNotBlank() -> "Sin resultados para \"$busqueda\""
            else -> "No hay productos en \"$categoria\""
        }
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {}
        )
    }
}