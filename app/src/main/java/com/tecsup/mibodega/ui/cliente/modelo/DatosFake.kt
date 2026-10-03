package com.tecsup.mibodega.ui.cliente.modelo

import com.tecsup.mibodega.R

/**
 * Datos de ejemplo (fake) que viven solo en memoria: se recrean cada vez que
 * se abre la app, no se guardan en ningún lado.
 *
 * Cada producto apunta a su foto en `res/drawable`. Cuando conecten Room o
 * una API, este archivo se reemplaza por un Repository real, pero las pantallas
 * no cambian porque ya reciben una `List<Producto>` como parámetro.
 */
/** Categoría "Todos": la que no filtra por categoría. */
const val CATEGORIA_TODOS = "Todos"

val listaCategorias = listOf(CATEGORIA_TODOS, "Bebidas", "Abarrotes", "Snacks")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra, grano largo, ideal para el día a día.",
        precio = 4.50,
        categoria = "Abarrotes",
        imagenResId = R.drawable.arroz
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
        precio = 8.90,
        categoria = "Abarrotes",
        imagenResId = R.drawable.aceite
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera 1 L.",
        precio = 5.20,
        categoria = "Abarrotes",
        imagenResId = R.drawable.leche
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate rellenas 126 g.",
        precio = 3.50,
        categoria = "Snacks",
        imagenResId = R.drawable.galleta_oreo
    ),
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
        precio = 6.50,
        categoria = "Bebidas",
        imagenResId = R.drawable.coca_colaaa
    )
)