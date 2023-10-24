package com.lucasxvirtual.financasmercado.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lucasxvirtual.financasmercado.data.local.ListConverters

@Entity(tableName = InvoiceEntity.TABLE_NAME)
@TypeConverters(ListConverters::class)
data class InvoiceEntity(
    @PrimaryKey val id: String,
    val marketCnpj: String,
    val totalPrice: Double,
    val date: String
) {
    companion object {
        const val TABLE_NAME = "invoice"
    }
}
