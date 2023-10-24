package com.lucasxvirtual.financasmercado.data

import com.lucasxvirtual.financasmercado.data.local.ProductInvoiceCompleteInformationLocalData
import com.lucasxvirtual.financasmercado.data.local.entities.AddressEntity
import com.lucasxvirtual.financasmercado.data.local.entities.InvoiceEntity
import com.lucasxvirtual.financasmercado.data.local.entities.MarketEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductInvoiceEntity
import com.lucasxvirtual.financasmercado.data.model.Address
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.Market
import com.lucasxvirtual.financasmercado.data.model.Product
import com.lucasxvirtual.financasmercado.data.model.ProductInvoice
import com.lucasxvirtual.financasmercado.data.model.ProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.remote.response.InvoiceResponse
import com.lucasxvirtual.financasmercado.data.remote.response.MarketResponse
import com.lucasxvirtual.financasmercado.data.remote.response.ProductInvoiceResponse
import com.lucasxvirtual.financasmercado.data.remote.response.ProductResponse
import com.lucasxvirtual.financasmercado.extensions.round

fun InvoiceResponse.toInvoice() = Invoice(
    id = id,
    market = marketResponse.toMarket(),
    productInvoiceList = productInvoiceList.map { it.toProductInvoice() },
    totalPrice = productInvoiceList.sumOf { (it.price * it.quantity - (it.discount ?: 0.0)).round() }.round(),
    date = date
)

fun Invoice.toInvoiceEntity() = InvoiceEntity(
    id = id,
    marketCnpj = market.cnpj,
    totalPrice = totalPrice,
    date = date
)

fun MarketResponse.toMarket() = Market(
    cnpj = cnpj,
    name = name,
    fantasyName = fantasyName,
    address = Address(
        name = addressResponse?.name,
        neighborhood = addressResponse?.neighborhood,
        cep = addressResponse?.cep,
        city = addressResponse?.city,
        cityCode = addressResponse?.cityCode,
        uf = addressResponse?.uf
    ),
    phone = phone
)

fun Market.toMarketEntity() = MarketEntity(
    cnpj = cnpj,
    name = name,
    fantasyName = fantasyName,
    address = AddressEntity(
        name = address?.name,
        neighborhood = address?.neighborhood,
        cep = address?.cep,
        city = address?.city,
        cityCode = address?.cityCode,
        uf = address?.uf
    ),
    phone = phone
)

fun MarketEntity?.toMarket() = Market(
    cnpj = this?.cnpj ?: "",
    name = this?.name ?: "",
    fantasyName = this?.fantasyName,
    address = Address(
        name = this?.address?.name,
        neighborhood = this?.address?.neighborhood,
        cep = this?.address?.cep,
        city = this?.address?.city,
        cityCode = this?.address?.cityCode,
        uf = this?.address?.uf
    ),
    phone = this?.phone
)

fun ProductResponse.toProduct() = Product(
    id, eanCode, name, unitType
)

fun Product.toProductEntity() = ProductEntity(
    id, eanCode, name, unitType
)

fun ProductEntity.toProduct() = Product(
    id, eanCode, name, unitType
)

fun ProductInvoiceResponse.toProductInvoice() = ProductInvoice(
    id = id,
    product = productResponse.toProduct(),
    price = price,
    quantity = quantity,
    discount = discount ?: 0.0
)

fun ProductInvoice.toProductInvoiceEntity(invoiceId: String) = ProductInvoiceEntity(
    id = id,
    productId = product.id,
    price = price,
    quantity = quantity,
    discount = discount,
    invoiceId = invoiceId
)

fun ProductInvoiceEntity.toProductInvoice(product: Product) = ProductInvoice(
    id = id,
    product = product,
    price = price,
    quantity = quantity,
    discount = discount
)

fun ProductInvoiceCompleteInformationLocalData.toProductInvoiceCompleteInfo() = ProductInvoiceCompleteInfo(
    productInvoice = productInvoice.toProductInvoice(product.toProduct()),
    date = date,
    invoiceId = productInvoice.invoiceId
)
