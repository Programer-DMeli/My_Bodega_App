package com.tecsup.mibodega.navegacion

/**
 * Definición de rutas y constantes de navegación para "Mi Bodega - App Cliente".
 * Sigue la arquitectura de Actividad Única (Single-Activity Architecture).
 */
object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val DATOS_ENTREGA = "datos_entrega"
    const val CONFIRMACION = "confirmacion"

    /**
     * Genera la ruta paramétrica para navegar a la pantalla de detalle de un producto.
     */
    fun detalle(productoId: Int): String = "detalle/$productoId"
}
