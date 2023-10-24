package com.lucasxvirtual.financasmercado.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ProductInvoiceEntity.TABLE_NAME)
data class ProductInvoiceEntity(
    @PrimaryKey val id: Int,
    val productId: Int,
    val invoiceId: String,
    val price: Double,
    val quantity: Double,
    val discount: Double
) {
    companion object {
        const val TABLE_NAME = "productInvoice"
    }
}
