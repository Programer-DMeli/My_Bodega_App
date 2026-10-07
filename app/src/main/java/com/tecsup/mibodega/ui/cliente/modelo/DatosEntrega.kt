package com.tecsup.mibodega.ui.cliente.modelo

data class DatosEntrega(
    val direccion: String,
    val referencia: String,
    val metodoPago: String,
    val horario: String
)

/** Opciones de pago del pedido. */
val listaMetodosPago = listOf("Tarjeta", "Efectivo", "Yape")

/** Franjas horarias de entrega. */
val listaHorarios = listOf("Hoy", "Mañana", "Programar")