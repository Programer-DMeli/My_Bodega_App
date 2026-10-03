package com.tecsup.mibodega.ui.cliente.modelo

/** Costo fijo del envío a domicilio. */
const val COSTO_DELIVERY = 4.00

/** Un ítem del carrito: producto + cuántas unidades se agregaron. */
data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
)

/**
 * Suma de las líneas del carrito (precio x cantidad).
 * Extensión de List: se usa en carrito, entrega y confirmación, siempre igual.
 */
val List<ItemCarrito>.subtotal: Double
    get() = sumOf { it.producto.precio * it.cantidad }

/** Delivery: no se cobra si el carrito está vacío. */
fun costoDelivery(carrito: List<ItemCarrito>): Double =
    if (carrito.isEmpty()) 0.0 else COSTO_DELIVERY

/** Total a pagar del pedido. */
val List<ItemCarrito>.total: Double
    get() = subtotal + costoDelivery(this)