package com.tecsup.mibodega.ui.cliente.modelo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas del filtro de productos.
 * Son funciones puras, así que se prueban sin Compose ni emulador.
 */
class FiltroProductosTest {

    private val catalogo = listaProductosFake

    @Test
    fun `sin filtros devuelve el catalogo completo`() {
        val resultado = filtrarProductos(catalogo, CATEGORIA_TODOS, "")

        assertEquals(catalogo.size, resultado.size)
    }

    @Test
    fun `filtra solo por categoria`() {
        val resultado = filtrarProductos(catalogo, "Bebidas", "")

        assertEquals(1, resultado.size)
        assertEquals("Coca-Cola Original", resultado.first().nombre)
    }

    @Test
    fun `filtra solo por texto sin importar mayusculas`() {
        val resultado = filtrarProductos(catalogo, CATEGORIA_TODOS, "COCA")

        assertEquals(1, resultado.size)
        assertEquals("Coca-Cola Original", resultado.first().nombre)
    }

    @Test
    fun `el texto busca en todas las categorias`() {
        // "Arroz" es de Abarrotes, pero si el usuario está en otra categoría
        // debe encontrarlo igual: la categoría no limita la búsqueda.
        val resultado = filtrarProductos(catalogo, "Bebidas", "arroz")

        assertEquals(1, resultado.size)
        assertEquals("Arroz Costeño", resultado.first().nombre)
    }

    @Test
    fun `la categoria vuelve a limitar cuando se borra la busqueda`() {
        val conTexto = filtrarProductos(catalogo, "Bebidas", "arroz")
        val sinTexto = filtrarProductos(catalogo, "Bebidas", "")

        assertEquals(1, conTexto.size)
        assertEquals("Coca-Cola Original", sinTexto.first().nombre)
        assertTrue(sinTexto.none { it.nombre == "Arroz Costeño" })
    }

    @Test
    fun `ignora espacios sobrantes en la busqueda`() {
        val resultado = filtrarProductos(catalogo, "Bebidas", "   leche  ")

        assertEquals(1, resultado.size)
        assertEquals("Leche Gloria", resultado.first().nombre)
    }

    @Test
    fun `busca tambien en la descripcion`() {
        val resultado = filtrarProductos(catalogo, "Snacks", "grano largo")

        assertEquals(1, resultado.size)
        assertEquals("Arroz Costeño", resultado.first().nombre)
    }
}