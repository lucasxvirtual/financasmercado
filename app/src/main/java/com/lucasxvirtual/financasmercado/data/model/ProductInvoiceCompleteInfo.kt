package com.lucasxvirtual.financasmercado.data.model

data class ProductInvoiceCompleteInfo(
    val productInvoice: ProductInvoice,
    val date: String,
    val invoiceId: String
) {
    val totalPrice = (productInvoice.price * productInvoice.quantity) - productInvoice.discount
}
