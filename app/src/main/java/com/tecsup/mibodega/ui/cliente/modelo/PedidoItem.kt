package com.tecsup.mibodega.ui.cliente.modelo

data class PedidoItem(
    val id: String,
    val fecha: String,
    val total: Double,
    val estado: String,
    val detalle: String,
    val items: List<ItemCarrito> = emptyList(),
    val direccion: String = "Av. Principal 123",
    val esDelivery: Boolean = true,
    val subtotal: Double = 0.0,
    val costoDelivery: Double = 4.0
)
