package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Representa un usuario registrado en la aplicación.
 */
data class Usuario(
    val nombre: String,
    val telefono: String,
    val clave: String,
    val direccion: String,
    val referencia: String
)
