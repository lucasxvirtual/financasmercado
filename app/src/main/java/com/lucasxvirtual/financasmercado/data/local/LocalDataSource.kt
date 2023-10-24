package com.lucasxvirtual.financasmercado.data.local

import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.ProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.toInvoiceEntity
import com.lucasxvirtual.financasmercado.data.toMarket
import com.lucasxvirtual.financasmercado.data.toMarketEntity
import com.lucasxvirtual.financasmercado.data.toProduct
import com.lucasxvirtual.financasmercado.data.toProductEntity
import com.lucasxvirtual.financasmercado.data.toProductInvoice
import com.lucasxvirtual.financasmercado.data.toProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.toProductInvoiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun saveInvoice(invoice: Invoice) {
        database.invoiceDao().insert(listOf(invoice.toInvoiceEntity()))
        database.marketDao().insert(listOf(invoice.market.toMarketEntity()))
        database.productDao().insert(invoice.productInvoiceList.map { it.product.toProductEntity() })
        database.productInvoiceDao().insert(
            invoice.productInvoiceList.map {
                it.toProductInvoiceEntity(invoice.id)
            }
        )
    }

    fun getInvoiceSimpleInformation() = database.invoiceDao().getInvoiceSimpleInformation()

    fun getInvoiceSimpleInformationByDate(date: String) =
        database.invoiceDao().getInvoiceSimpleInformationByDate(date)

    fun getLastInvoice(): Flow<Invoice?> {
        return database.invoiceDao().getLastInvoice().map {
            if (it == null) {
                return@map null
            }
            val marketEntity = database.marketDao().getByCnpj(it.marketCnpj)
            val productInvoiceAndProductList = database.productInvoiceDao().getByInvoiceId(it.id)
            val productInvoiceList = productInvoiceAndProductList.map { value ->
                value.productInvoice.toProductInvoice(value.product.toProduct())
            }
            Invoice(
                id = it.id,
                market = marketEntity.firstOrNull().toMarket(),
                date = it.date,
                totalPrice = it.totalPrice,
                productInvoiceList = productInvoiceList
            )
        }
    }

    fun getInvoice(invoiceId: String): Flow<Invoice?> {
        return database.invoiceDao().getInvoice(invoiceId).map {
            if (it == null) {
                return@map null
            }
            val marketEntity = database.marketDao().getByCnpj(it.marketCnpj)
            val productInvoiceAndProductList =
                database.productInvoiceDao().getByInvoiceId(it.id)
            val productInvoiceList = productInvoiceAndProductList.map { value ->
                value.productInvoice.toProductInvoice(value.product.toProduct())
            }
            Invoice(
                id = it.id,
                market = marketEntity.firstOrNull().toMarket(),
                date = it.date,
                totalPrice = it.totalPrice,
                productInvoiceList = productInvoiceList
            )
        }
    }

    fun getMostSpentProducts() = database.productInvoiceDao().getByMostSpent()

    fun getMonthlyReport() = database.invoiceDao().getMonthlyReport()

    fun getProductCompleteInfo(productId: Int): Flow<List<ProductInvoiceCompleteInfo>> {
        return database.productInvoiceDao().getByProductId(productId).map {
            it.map { list ->
                list.toProductInvoiceCompleteInfo()
            }
        }
    }

    suspend fun clear() {
        database.invoiceDao().deleteAll()
        database.marketDao().deleteAll()
        database.productDao().deleteAll()
        database.productInvoiceDao().deleteAll()
    }
}