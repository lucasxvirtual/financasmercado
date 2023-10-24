package com.lucasxvirtual.financasmercado.data.model

data class Market(
    val cnpj: String,
    val name: String,
    val fantasyName: String?,
    val address: Address?,
    val phone: String?
)

data class Address(
    val name: String?,
    val neighborhood: String?,
    val cep: String?,
    val city: String?,
    val cityCode: Int?,
    val uf: String?
) {
    val formattedName: String
        get() = "$name, $neighborhood - $city"
}
