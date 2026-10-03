package com.tecsup.mibodega.navegacion

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.DatosEntrega
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.modelo.total
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.ProductoNoEncontradoScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PantallaPendiente

/**
 * "Director de orquesta" de la navegación en la arquitectura Single-Activity.
 *
 * - Instancia el NavController con [rememberNavController].
 * - Configura el [NavHost] vinculando las [Rutas] con cada pantalla Compose.
 * - Administra el estado global del carrito mediante State Hoisting.
 *
 * Además hospeda el `Scaffold` principal de la app: la NavigationBar va en su
 * `bottomBar` y envuelve al NavHost, de modo que la barra sigue visible
 * mientras el usuario salta entre las pestañas principales (Inicio, Categorías,
 * Pedidos y Perfil) y desaparece en las pantallas de flujo (Detalle, Carrito...).
 *
 * Sin ViewModel: el estado vive en `remember { mutableStateOf(...) }` aquí arriba,
 * y cada pantalla es "hoja" (stateless) que solo recibe datos y callbacks.
 */
@Composable
fun AppNavegacion() {

    // 1) El controlador de navegación sobrevive a las recomposiciones.
    val navController = rememberNavController()

    // 2) Estado global del carrito. Se declara acá arriba porque lo usan
    //    varias pantallas (Inicio, Detalle, Carrito): es el "state hoisting".
    //    rememberSaveable: con "remember" el carrito se perdería al rotar el
    //    celular, porque un cambio de configuración recrea la Activity.
    var carrito by rememberSaveable(stateSaver = carritoSaver) {
        mutableStateOf<List<ItemCarrito>>(emptyList())
    }

    // Datos que captura la pantalla de entrega y lee la confirmación.
    var datosEntrega by rememberSaveable(stateSaver = datosEntregaSaver) {
        mutableStateOf(
            DatosEntrega(direccion = "", referencia = "", metodoPago = "", horario = "")
        )
    }
    var codigoPedido by rememberSaveable { mutableStateOf("") }
    // El total se congela al confirmar: el carrito se vacía al volver al inicio.
    var totalPedido by rememberSaveable { mutableStateOf(0.0) }

    // 3) Destino visible ahora mismo: es lo que marca la pestaña seleccionada
    //    en la barra (el estado del NavHost leído como State, sin ViewModel).
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // 4) Scaffold de la app: la barra inferior vive aquí y no dentro de cada
    //    pantalla, para que no desaparezca al cambiar de pestaña.
    Scaffold(
        // Cada pantalla ya pide sus insets con safeDrawingPadding(), así que
        // el Scaffold no debe añadirlos otra vez (evita el doble padding).
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Solo en las pestañas principales tiene sentido la barra.
            if (Rutas.esRutaPrincipal(rutaActual)) {
                BarraNavegacion(
                    destinoActual = rutaActual.orEmpty(),
                    onDestinoSeleccionado = { ruta ->
                        navController.navigate(ruta) {
                            // El stack de pestañas se apoya siempre en Inicio (no
                            // en Bienvenida, que ya se cerró al entrar): así el
                            // "atrás" desde Inicio sale de la app.
                            popUpTo(Rutas.INICIO) { saveState = true }
                            // No apilar la misma pestaña dos veces y recuperar
                            // el estado previo (scroll, filtros) de esa pestaña.
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingInterno ->

        // 5) El NavHost es el equivalente al "fragment container" de la
        //    arquitectura clásica, pero aquí cada destino es una función @Composable.
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            modifier = Modifier.padding(paddingInterno)
        ) {

            // ---- Pantalla 1: Bienvenida ----
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                    onTerminos = { /* TODO: abrir términos y condiciones */ }
                )
            }

            // ---- Pantalla 2: Login ----
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onVolver = { navController.popBackStack() },
                    onIniciarSesion = { _, _ ->
                        // Sesión iniciada: se limpia el welcome para que el "atrás"
                        // del sistema no devuelva al usuario a la pantalla inicial.
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
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
                    onCrearCuenta = { _, _, _, _ ->
                        // Al crear la cuenta no tiene sentido volver a Bienvenida.
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                )
            }

            // ---- Pestaña 1: Inicio / catálogo ----
            composable(Rutas.INICIO) {
                InicioScreen(
                    productos = listaProductosFake,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onProductoClick = { producto ->
                        // Ruta paramétrica: el id se interpola en la ruta
                        // (Rutas.detalle(5) -> "detalle/5").
                        navController.navigate(Rutas.detalle(producto.id)) {
                            launchSingleTop = true
                        }
                    },
                    onAgregarProducto = { producto ->
                        carrito = agregarOSumarProducto(carrito, producto, 1)
                    }
                )
            }

            // ---- Pestaña 2: Categorías (se completa en su paso) ----
            composable(Rutas.CATEGORIAS) {
                PantallaPendiente(
                    titulo = "Categorías",
                    icono = Icons.AutoMirrored.Filled.List
                )
            }

            // ---- Pestaña 3: Pedidos (se completa en su paso) ----
            composable(Rutas.PEDIDOS) {
                PantallaPendiente(
                    titulo = "Mis pedidos",
                    icono = Icons.Default.Receipt
                )
            }

            // ---- Pestaña 4: Perfil (se completa en su paso) ----
            composable(Rutas.PERFIL) {
                PantallaPendiente(
                    titulo = "Mi perfil",
                    icono = Icons.Default.Person
                )
            }

            // ---- Pantalla 5: Detalle del producto (ruta paramétrica) ----
            // El id viaja dentro de la ruta: "detalle/5". La pantalla NO recibe
            // el id, recibe el Producto ya buscado (ver más abajo).
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
                    // El id llegó bien tipado pero no existe en el catálogo.
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
                DatosEntregaScreen(
                    carrito = carrito,
                    onVolver = { navController.popBackStack() },
                    onConfirmar = { datos ->
                        datosEntrega = datos
                        codigoPedido = generarCodigoPedido()
                        totalPedido = carrito.total
                        // popUpTo: se borra el historial del flujo de compra
                        // (carrito y datos de entrega) y la confirmación queda
                        // como única pantalla: el usuario no puede retroceder
                        // hacia el carrito desde el cierre del pedido.
                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ---- Pantalla 8: Confirmación ----
            composable(Rutas.CONFIRMACION) {
                ConfirmacionScreen(
                    codigoPedido = codigoPedido,
                    datosEntrega = datosEntrega,
                    total = totalPedido,
                    onVolverAlInicio = {
                        // Cierre del flujo: carrito vacío y pila limpia, para
                        // empezar de cero al volver al catálogo.
                        carrito = emptyList()
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.CONFIRMACION) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

/** Código de pedido fake: "MB-" seguido de 4 dígitos. */
private fun generarCodigoPedido(): String =
    "MB-" + (1000..9999).random()

/**
 * El carrito no es un tipo que Android sepa guardar por sí solo, así que se
 * guarda solo lo indispensable: la lista plana [id, cantidad, id, cantidad...].
 * Al restaurar, cada id se busca en el catálogo para volver a armar el Producto.
 */
private val carritoSaver = listSaver<List<ItemCarrito>, Int>(
    save = { items -> items.flatMap { listOf(it.producto.id, it.cantidad) } },
    restore = { datos ->
        datos.chunked(2).mapNotNull { par ->
            val producto = listaProductosFake.firstOrNull { it.id == par[0] }
            producto?.let { ItemCarrito(producto = it, cantidad = par[1]) }
        }
    }
)

/** Mismo criterio para los datos de entrega: solo texto, que sí es guardable. */
private val datosEntregaSaver = listSaver<DatosEntrega, String>(
    save = { datos ->
        listOf(datos.direccion, datos.referencia, datos.metodoPago, datos.horario)
    },
    restore = { datos ->
        DatosEntrega(
            direccion = datos[0] as String,
            referencia = datos[1] as String,
            metodoPago = datos[2] as String,
            horario = datos[3] as String
        )
    }
)

/**
 * Agrega el producto al carrito; si ya existe, solo suma la cantidad.
 * Función pura: recibe el carrito actual y devuelve uno nuevo.
 */
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