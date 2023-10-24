package com.lucasxvirtual.financasmercado.data.model

data class Product(
    val id: Int,
    val eanCode: String?,
    val name: String,
    val unitType: String
)