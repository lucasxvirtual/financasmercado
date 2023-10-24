package com.lucasxvirtual.financasmercado.ui.product

import com.lucasxvirtual.financasmercado.data.model.ProductInvoiceCompleteInfo

sealed class ProductUIState(
    val productName: String
) {
    class FilledProductUIState(
        productName: String,
        val productInvoiceList: List<ProductInvoiceCompleteInfo>
    ): ProductUIState(productName) {
        val mostExpensivePurchase = productInvoiceList.maxBy { it.productInvoice.price }
        val leastExpensivePurchase = productInvoiceList.minBy { it.productInvoice.price }
        val unityType =
            productInvoiceList.first().productInvoice.product.unitType
        val totalQuantity = productInvoiceList.sumOf { it.productInvoice.quantity }
        val totalSpent =
            productInvoiceList.sumOf { (it.productInvoice.quantity * it.productInvoice.price) - it.productInvoice.discount }
    }
    class EmptyProductUIState(productName: String): ProductUIState(productName)
    class LoadingProductUIState(productName: String): ProductUIState(productName)
}
