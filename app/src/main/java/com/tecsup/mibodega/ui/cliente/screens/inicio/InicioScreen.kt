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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.CATEGORIA_TODOS
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.filtrarProductos
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.FilaCategorias
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class OrdenPrecio {
    DEFECTO, MENOR_MAYOR, MAYOR_MENOR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    favoritosIds: Set<Int>,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onToggleFavorito: (Producto) -> Unit
) {
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(listaCategorias.first()) }
    var textoBusqueda by rememberSaveable { mutableStateOf("") }
    var ordenPrecio by rememberSaveable { mutableStateOf(OrdenPrecio.DEFECTO) }
    var mostrarMenuOrden by remember { mutableStateOf(false) }

    val productosFiltrados = remember(productos, categoriaSeleccionada, textoBusqueda, ordenPrecio) {
        val filtrados = filtrarProductos(
            productos = productos,
            categoriaSeleccionada = categoriaSeleccionada,
            textoBusqueda = textoBusqueda
        )
        when (ordenPrecio) {
            OrdenPrecio.MENOR_MAYOR -> filtrados.sortedBy { it.precio }
            OrdenPrecio.MAYOR_MENOR -> filtrados.sortedByDescending { it.precio }
            OrdenPrecio.DEFECTO -> filtrados
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { mostrarMenuOrden = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Ordenar por precio")
                    }
                    DropdownMenu(
                        expanded = mostrarMenuOrden,
                        onDismissRequest = { mostrarMenuOrden = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ordenar por defecto") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.DEFECTO
                                mostrarMenuOrden = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Precio: menor a mayor") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.MENOR_MAYOR
                                mostrarMenuOrden = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Precio: mayor a menor") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.MAYOR_MENOR
                                mostrarMenuOrden = false
                            }
                        )
                    }

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

            FilaCategorias(
                categorias = listaCategorias,
                categoriaSeleccionada = categoriaSeleccionada,
                onCategoriaSeleccionada = { categoriaSeleccionada = it }
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados, key = { producto -> producto.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) },
                        esFavorito = producto.id in favoritosIds,
                        onFavoritoClick = { onToggleFavorito(producto) }
                    )
                }

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
        val mensaje = when {
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
