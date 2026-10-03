package com.tecsup.mibodega.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

/**
 * "Director de orquesta" de la navegación en la arquitectura Single-Activity.
 *
 * - Instancia el NavController con [rememberNavController].
 * - Configura el [NavHost] vinculando las [Rutas] con cada pantalla Compose.
 * - Administra el estado global del carrito mediante State Hoisting.
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
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // 3) El NavHost es el equivalente al "fragment container" de la arquitectura
    //    clásica, pero aquí cada destino es una función @Composable.
    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {

        // ---- Pantalla 1: Bienvenida ----
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.INICIO) },
                onTerminos = { /* TODO: abrir términos y condiciones */ }
            )
        }

        // ---- Pantalla 2: Registro ----
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

        // ---- Pantalla 3: Inicio / catálogo ----
        composable(Rutas.INICIO) {
            InicioScreen(
                productos = listaProductosFake,
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                }
            )
        }

        // ---- Pantalla 4: Detalle del producto (ruta paramétrica) ----
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument(Rutas.ARG_PRODUCTO_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt(Rutas.ARG_PRODUCTO_ID) ?: 0
            val producto = listaProductosFake.firstOrNull { it.id == productoId }
                ?: listaProductosFake.first()

            DetalleProductoScreen(
                producto = producto,
                onVolver = { navController.popBackStack() },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                    carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                    navController.popBackStack()
                }
            )
        }

        // ---- Pantalla 5: Carrito ----
        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) {
                            it.copy(cantidad = it.cantidad + 1)
                        } else {
                            it
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

        // ---- Pantalla 6: Datos de entrega (se implementa en el paso 6) ----
        composable(Rutas.DATOS_ENTREGA) {

        }

        // ---- Pantalla 7: Confirmación (se implementa en el paso 7) ----
        composable(Rutas.CONFIRMACION) {

        }
    }
}

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