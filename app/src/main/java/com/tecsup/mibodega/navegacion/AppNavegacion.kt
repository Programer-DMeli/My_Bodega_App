package com.tecsup.mibodega.navegacion

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.ProductoNoEncontradoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.modelo.PedidoItem
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PantallaPendiente
import com.tecsup.mibodega.ui.theme.BodegaTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var usuariosRegistrados by remember {
        mutableStateOf<Map<String, Usuario>>(
            mapOf("987654321" to Usuario("Meliton", "987654321", "123456", "Av. Principal 123", "Frente al parque"))
        )
    }
    var favoritosIds by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var pedidosList by remember { mutableStateOf<List<PedidoItem>>(emptyList()) }
    var ultimoPedido by remember { mutableStateOf<PedidoItem?>(null) }
    var isDarkMode by remember { mutableStateOf(false) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    BodegaTheme(darkTheme = isDarkMode) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (Rutas.esRutaPrincipal(rutaActual)) {
                    BarraNavegacion(
                        destinoActual = rutaActual.orEmpty(),
                        onDestinoSeleccionado = { ruta ->
                            navController.navigate(ruta) {
                                popUpTo(Rutas.INICIO) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingInterno ->
            NavHost(
                navController = navController,
                startDestination = Rutas.BIENVENIDA,
                enterTransition = { fadeIn(animationSpec = tween(250)) },
                exitTransition = { fadeOut(animationSpec = tween(250)) },
                popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                popExitTransition = { fadeOut(animationSpec = tween(250)) },
                modifier = Modifier.padding(paddingInterno)
            ) {

                // ---- Pantalla 1: Bienvenida ----
                composable(Rutas.BIENVENIDA) {
                    BienvenidaScreen(
                        onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                        onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                        onTerminos = { }
                    )
                }

                // ---- Pantalla 2: Login ----
                composable(Rutas.LOGIN) {
                    LoginScreen(
                        onVolver = { navController.popBackStack() },
                        onIniciarSesion = { telefono, clave ->
                            val usuario = usuariosRegistrados[telefono]
                            if (usuario != null && usuario.clave == clave) {
                                navController.navigate(Rutas.INICIO) {
                                    popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                                }
                            }
                        },
                        onCrearCuenta = {
                            navController.navigate(Rutas.REGISTRO) {
                                popUpTo(Rutas.LOGIN) { inclusive = true }
                            }
                        }
                    )
                }

                // ---- Pantalla 3: Crear cuenta ----
                composable(Rutas.REGISTRO) {
                    RegistroScreen(
                        onVolver = { navController.popBackStack() },
                        onCrearCuenta = { nombre, telefono, direccion, referencia ->
                            val nuevoUsuario = Usuario(nombre, telefono, "123456", direccion, referencia)
                            usuariosRegistrados = usuariosRegistrados + (telefono to nuevoUsuario)
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.REGISTRO) { inclusive = true }
                            }
                        }
                    )
                }

                // ---- Pestaña 1: Inicio / catálogo ----
                composable(Rutas.INICIO) {
                    InicioScreen(
                        productos = listaProductosFake,
                        cantidadCarrito = carrito.sumOf { it.cantidad },
                        favoritosIds = favoritosIds,
                        onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                        onProductoClick = { producto ->
                            navController.navigate(Rutas.detalle(producto.id)) {
                                launchSingleTop = true
                            }
                        },
                        onAgregarProducto = { producto ->
                            carrito = agregarOSumarProducto(carrito, producto, 1)
                        },
                        onToggleFavorito = { producto ->
                            favoritosIds = if (producto.id in favoritosIds) {
                                favoritosIds - producto.id
                            } else {
                                favoritosIds + producto.id
                            }
                        }
                    )
                }

                // ---- Pestaña 2: Categorías ----
                composable(Rutas.CATEGORIAS) {
                    PantallaPendiente(
                        titulo = "Categorías",
                        icono = Icons.AutoMirrored.Filled.List
                    )
                }

                // ---- Pestaña 3: Pedidos ----
                composable(Rutas.PEDIDOS) {
                    PedidosScreen(
                        pedidos = pedidosList,
                        onVolver = { navController.popBackStack() }
                    )
                }

                // ---- Pestaña 4: Perfil ----
                composable(Rutas.PERFIL) {
                    PerfilScreen(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = it },
                        onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                        onVerPedidos = { navController.navigate(Rutas.PEDIDOS) },
                        onCerrarSesion = {
                            navController.navigate(Rutas.BIENVENIDA) {
                                popUpTo(Rutas.INICIO) { inclusive = true }
                            }
                        }
                    )
                }

                // ---- Pantalla Favoritos ----
                composable(Rutas.FAVORITOS) {
                    FavoritosScreen(
                        favoritosIds = favoritosIds,
                        productos = listaProductosFake,
                        onVolver = { navController.popBackStack() },
                        onProductoClick = { producto ->
                            navController.navigate(Rutas.detalle(producto.id)) {
                                launchSingleTop = true
                            }
                        },
                        onAgregarProducto = { producto ->
                            carrito = agregarOSumarProducto(carrito, producto, 1)
                        }
                    )
                }

                // ---- Pantalla 5: Detalle del producto ----
                composable(
                    route = Rutas.DETALLE,
                    arguments = listOf(
                        navArgument(Rutas.ARG_PRODUCTO_ID) {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntryDetalle ->
                    val productoId =
                        backStackEntryDetalle.arguments?.getInt(Rutas.ARG_PRODUCTO_ID) ?: 0
                    val producto = listaProductosFake.firstOrNull { it.id == productoId }

                    if (producto == null) {
                        ProductoNoEncontradoScreen(onVolver = { navController.popBackStack() })
                    } else {
                        DetalleProductoScreen(
                            producto = producto,
                            onVolver = { navController.popBackStack() },
                            onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                                carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // ---- Pantalla 6: Carrito ----
                composable(Rutas.CARRITO) {
                    CarritoScreen(
                        carrito = carrito,
                        onVolver = { navController.popBackStack() },
                        onIncrementar = { producto ->
                            carrito = carrito.map { item ->
                                if (item.producto.id == producto.id) {
                                    item.copy(cantidad = item.cantidad + 1)
                                } else {
                                    item
                                }
                            }
                        },
                        onDecrementar = { producto ->
                            carrito = carrito.mapNotNull { item ->
                                when {
                                    item.producto.id != producto.id -> item
                                    item.cantidad > 1 -> item.copy(cantidad = item.cantidad - 1)
                                    else -> null
                                }
                            }
                        },
                        onEliminar = { producto ->
                            carrito = carrito.filterNot { it.producto.id == producto.id }
                        },
                        onContinuarPedido = { navController.navigate(Rutas.DATOS_ENTREGA) }
                    )
                }

                // ---- Pantalla 7: Datos de entrega ----
                composable(Rutas.DATOS_ENTREGA) {
                    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
                    DatosEntregaScreen(
                        onVolver = { navController.popBackStack() },
                        onConfirmarPedido = { esDelivery ->
                            val costoEnvio = if (esDelivery) 4.0 else 0.0
                            val totalFinal = subtotal + costoEnvio

                            val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            val fechaActual = formatoFecha.format(Date())
                            val detalleStr = carrito.joinToString(", ") { "${it.cantidad}x ${it.producto.nombre}" }

                            val nuevoPedido = PedidoItem(
                                id = (pedidosList.size + 1001).toString(),
                                fecha = fechaActual,
                                total = totalFinal,
                                estado = "Confirmado",
                                detalle = detalleStr,
                                items = carrito,
                                direccion = "Av. Principal 123",
                                esDelivery = esDelivery,
                                subtotal = subtotal,
                                costoDelivery = costoEnvio
                            )
                            ultimoPedido = nuevoPedido
                            pedidosList = listOf(nuevoPedido) + pedidosList
                            carrito = emptyList()

                            navController.navigate(Rutas.CONFIRMACION) {
                                popUpTo(Rutas.CARRITO) { inclusive = true }
                            }
                        }
                    )
                }

                // ---- Pantalla 8: Confirmación ----
                composable(Rutas.CONFIRMACION) {
                    ConfirmacionScreen(
                        pedido = ultimoPedido,
                        onVolverInicio = {
                            navController.navigate(Rutas.INICIO) {
                                popUpTo(Rutas.INICIO) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.firstOrNull { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map { item ->
            if (item.producto.id == producto.id) {
                item.copy(cantidad = item.cantidad + cantidad)
            } else {
                item
            }
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}
