package com.tecsup.mibodega.navegacion

/**
 * Definición de rutas y constantes de navegación para "Mi Bodega - App Cliente".
 * Sigue la arquitectura de Actividad Única (Single-Activity Architecture):
 * una sola Activity ([com.tecsup.mibodega.MainActivity]) y un solo NavHost,
 * donde cada `composable(...)` es una pantalla.
 *
 * Reglas:
 * - Cada ruta es una `const val` para que nunca haya typos al navegar.
 * - Las rutas con parámetros usan `{argumento}` y se construyen con la función
 *   helper correspondiente (ej. [detalle]), nunca escribiendo la ruta a mano.
 * - Los nombres de los argumentos también son constantes ([ARG_PRODUCTO_ID])
 *   y deben coincidir con los usados en `navArgument(...)`.
 */
object Rutas {

    /** Pantalla 1: bienvenida / login / registro. Punto de entrada de la app. */
    const val BIENVENIDA = "bienvenida"

    /** Pantalla 2: iniciar sesión con celular y contraseña. */
    const val LOGIN = "login"

    /** Pantalla 3: formulario para crear la cuenta. */
    const val REGISTRO = "registro"

    /** Pantalla 4: catálogo de productos (home del cliente). */
    const val INICIO = "inicio"

    /** Pantalla 5: detalle de un producto. Es paramétrica. */
    const val DETALLE = "detalle/{productoId}"

    /** Pantalla 6: carrito de compras. */
    const val CARRITO = "carrito"

    /** Pantalla 7: formulario de datos de entrega. */
    const val DATOS_ENTREGA = "datos_entrega"

    /** Pantalla 8: confirmación del pedido. */
    const val CONFIRMACION = "confirmacion"

    /** Nombre del argumento que viaja en la ruta [DETALLE]. */
    const val ARG_PRODUCTO_ID = "productoId"

    /**
     * Genera la ruta paramétrica para navegar a la pantalla de detalle de un producto.
     * Ejemplo: `Rutas.detalle(3)` -> "detalle/3"
     */
    fun detalle(productoId: Int): String = "detalle/$productoId"
}