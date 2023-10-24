package com.lucasxvirtual.financasmercado.data.model

data class Invoice(
    val id: String,
    val market: Market,
    val productInvoiceList: List<ProductInvoice>,
    val totalPrice: Double,
    val date: String
)
