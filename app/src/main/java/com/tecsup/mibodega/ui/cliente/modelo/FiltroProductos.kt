package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Filtro de productos por categoría Y texto, aplicados a la vez.
 *
 * Es una función pura: recibe la lista completa y devuelve la lista filtrada.
 * Al vivir fuera de la pantalla se puede probar sin Compose ni Android.
 *
 * Los dos criterios se combinan con AND: el producto debe cumplir ambos,
 * así escribir "coca" mientras está en "Bebidas" no borra el filtro de categoría
 * (ni al revés). Si un criterio no se usa, no restringe el resultado.
 *
 * @param productos lista completa del catálogo
 * @param categoriaSeleccionada categoría activa, o [CATEGORIA_TODOS] para no filtrar
 * @param textoBusqueda texto escrito por el usuario (vacío = no filtra)
 */
fun filtrarProductos(
    productos: List<Producto>,
    categoriaSeleccionada: String,
    textoBusqueda: String
): List<Producto> {
    val consulta = textoBusqueda.trim().lowercase()

    return productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == CATEGORIA_TODOS ||
            producto.categoria == categoriaSeleccionada

        val coincideTexto = consulta.isEmpty() ||
            producto.nombre.lowercase().contains(consulta) ||
            producto.descripcion.lowercase().contains(consulta)

        coincideCategoria && coincideTexto
    }
}