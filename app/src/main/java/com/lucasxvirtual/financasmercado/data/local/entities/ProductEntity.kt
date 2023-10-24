package com.lucasxvirtual.financasmercado.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ProductEntity.TABLE_NAME)
data class ProductEntity(
    @PrimaryKey val id: Int,
    val eanCode: String?,
    val name: String,
    val unitType: String
) {
    companion object {
        const val TABLE_NAME = "product"
    }
}
