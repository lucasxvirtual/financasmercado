package com.lucasxvirtual.financasmercado.data.local

import androidx.room.Embedded
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductInvoiceEntity

data class ProductInvoiceAndProduct(
    @Embedded val productInvoice: ProductInvoiceEntity,
    @Embedded(prefix = "p") val product: ProductEntity
)