package com.tecsup.mibodega.ui.cliente.modelo

import androidx.annotation.DrawableRes

/**
 * Producto de la bodega.
 *
 * @param imagenResId drawable del producto (hoy local, mañanaauri de la API)
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    @DrawableRes val imagenResId: Int
)