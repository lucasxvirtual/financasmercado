package com.lucasxvirtual.financasmercado.data.model

import com.lucasxvirtual.financasmercado.extensions.round

data class ProductInvoice(
    val id: Int,
    val product: Product,
    val price: Double,
    val quantity: Double,
    val discount: Double
) {
    val roundedTotalPrice = (price * quantity).round()
}
