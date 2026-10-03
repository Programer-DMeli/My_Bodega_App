package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Filtro de productos por texto y categoría.
 *
 * Es una función pura: recibe la lista completa y devuelve la lista filtrada.
 * Al vivir fuera de la pantalla se puede probar sin Compose ni Android.
 *
 * Reglas:
 * - **Con texto escrito la búsqueda es global**: la categoría deja de limitar,
 *   así "arroz" encuentra el arroz aunque estés en la pestaña "Bebidas".
 * - **Sin texto manda la categoría**: el filtro es por categoría, o todos los
 *   productos si está en [CATEGORIA_TODOS].
 *
 * El texto se busca en el nombre y en la descripción, sin distinguir
 * mayúsculas y sin ignorar espacios sobrantes.
 *
 * @param productos lista completa del catálogo
 * @param categoriaSeleccionada categoría activa, o [CATEGORIA_TODOS] para no filtrar
 * @param textoBusqueda texto escrito por el usuario (vacío = solo categoría)
 */
fun filtrarProductos(
    productos: List<Producto>,
    categoriaSeleccionada: String,
    textoBusqueda: String
): List<Producto> {
    val consulta = textoBusqueda.trim().lowercase()

    // Si hay algo escrito, la búsqueda atraviesa todas las categorías.
    val buscarEnTodasLasCategorias = consulta.isNotEmpty()

    return productos.filter { producto ->
        val coincideTexto = consulta.isEmpty() ||
            producto.nombre.lowercase().contains(consulta) ||
            producto.descripcion.lowercase().contains(consulta)

        val coincideCategoria = buscarEnTodasLasCategorias ||
            categoriaSeleccionada == CATEGORIA_TODOS ||
            producto.categoria == categoriaSeleccionada

        coincideTexto && coincideCategoria
    }
}