package com.lucasxvirtual.financasmercado.data.remote.response

import com.google.gson.annotations.SerializedName

data class SubmitInvoiceResponse(
    @SerializedName("invoice") val invoiceResponse: InvoiceResponse
)

data class ProductResponse(
    val id: Int,
    val eanCode: String?,
    val name: String,
    val unitType: String
)

data class MarketResponse(
    val cnpj: String,
    val name: String,
    val fantasyName: String?,
    @SerializedName("address") val addressResponse: AddressResponse?,
    val phone: String?
)

data class AddressResponse(
    val id: Int,
    val name: String?,
    val neighborhood: String?,
    val cep: String?,
    val city: String?,
    val cityCode: Int?,
    val uf: String?
)

data class ProductInvoiceResponse(
    val id: Int,
    @SerializedName("product") val productResponse: ProductResponse,
    val price: Double,
    val quantity: Double,
    val discount: Double?
)

data class InvoiceResponse(
    val id: String,
    @SerializedName("market") val marketResponse: MarketResponse,
    @SerializedName("products") val productInvoiceList: List<ProductInvoiceResponse>,
    val date: String
)
