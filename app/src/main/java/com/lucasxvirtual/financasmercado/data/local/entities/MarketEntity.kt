package com.lucasxvirtual.financasmercado.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MarketEntity.TABLE_NAME)
data class MarketEntity(
    @PrimaryKey val cnpj: String,
    val name: String,
    val fantasyName: String?,
    @Embedded val address: AddressEntity?,
    val phone: String?
) {
    companion object {
        const val TABLE_NAME = "market"
    }
}

data class AddressEntity(
    @ColumnInfo(name = "addressName") val name: String?,
    val neighborhood: String?,
    val cep: String?,
    val city: String?,
    val cityCode: Int?,
    val uf: String?
)
