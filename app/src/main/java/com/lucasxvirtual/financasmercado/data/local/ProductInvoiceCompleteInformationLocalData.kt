package com.lucasxvirtual.financasmercado.data.local

import androidx.room.Embedded
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductInvoiceEntity

data class ProductInvoiceCompleteInformationLocalData(
    @Embedded val productInvoice: ProductInvoiceEntity,
    @Embedded("p") val product: ProductEntity,
    val date: String
)
