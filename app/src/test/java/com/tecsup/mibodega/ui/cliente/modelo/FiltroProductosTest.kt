package com.tecsup.mibodega.ui.cliente.modelo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas del filtro combinado categoría + texto.
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
    fun `texto y categoria se combinan con AND`() {
        // "Arroz" es de Abarrotes: coincide con ambas condiciones.
        val conAbarrotes = filtrarProductos(catalogo, "Abarrotes", "arroz")
        assertEquals(1, conAbarrotes.size)
        assertEquals("Arroz Costeño", conAbarrotes.first().nombre)

        // El mismo texto con otra categoría no debe devolver nada:
        // el filtro de categoría no se reemplaza por el de texto.
        val conBebidas = filtrarProductos(catalogo, "Bebidas", "arroz")
        assertTrue(conBebidas.isEmpty())
    }

    @Test
    fun `ignora espacios sobrantes en la busqueda`() {
        val resultado = filtrarProductos(catalogo, CATEGORIA_TODOS, "   leche  ")

        assertEquals(1, resultado.size)
        assertEquals("Leche Gloria", resultado.first().nombre)
    }

    @Test
    fun `busca tambien en la descripcion`() {
        val resultado = filtrarProductos(catalogo, CATEGORIA_TODOS, "grano largo")

        assertEquals(1, resultado.size)
        assertEquals("Arroz Costeño", resultado.first().nombre)
    }
}