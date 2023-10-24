package com.lucasxvirtual.financasmercado.data

import com.lucasxvirtual.financasmercado.Constants.IO
import com.lucasxvirtual.financasmercado.data.local.LocalDataSource
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation
import com.lucasxvirtual.financasmercado.data.model.MonthlyReport
import com.lucasxvirtual.financasmercado.data.model.ProductInvoice
import com.lucasxvirtual.financasmercado.data.model.ProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.model.SpentOnProductReport
import com.lucasxvirtual.financasmercado.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class Repository @Inject constructor(
    @Named(IO) private val dispatcher: CoroutineDispatcher,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun submitInvoice(invoiceNumber: String): Invoice {
        return withContext(dispatcher) {
            val invoice = remoteDataSource.submitInvoice(invoiceNumber)
            localDataSource.saveInvoice(invoice)
            invoice
        }
    }

    suspend fun submitAll(invoiceIds: List<String>) {
        withContext(dispatcher) {
            invoiceIds.forEach {
                val invoice = remoteDataSource.submitInvoice(it)
                localDataSource.saveInvoice(invoice)
            }
        }
    }

    fun getLastInvoices(): Flow<List<InvoiceSimpleInformation>> {
        return localDataSource.getInvoiceSimpleInformation()
    }

    fun getInvoicesFromMonth(date: String): Flow<List<InvoiceSimpleInformation>> {
        return localDataSource.getInvoiceSimpleInformationByDate(date)
    }

    fun getLastInvoice(): Flow<Invoice?> {
        return localDataSource.getLastInvoice().map {
            it?.copy(
                productInvoiceList = joinProducts(it.productInvoiceList)
            )
        }
    }

    fun getInvoice(invoiceId: String): Flow<Invoice?> {
        return localDataSource.getInvoice(invoiceId).map {
            it?.copy(
                productInvoiceList = joinProducts(it.productInvoiceList)
            )
        }
    }

    fun getMostSpentProducts(): Flow<List<SpentOnProductReport>> {
        return localDataSource.getMostSpentProducts()
    }

    fun getMonthlyReport(): Flow<List<MonthlyReport>> {
        return localDataSource.getMonthlyReport()
    }

    fun getProductCompleteInfo(productId: Int): Flow<List<ProductInvoiceCompleteInfo>> {
        return localDataSource.getProductCompleteInfo(productId).map {
            joinProductInvoiceCompleteInfo(it)
        }
    }

    suspend fun clear() {
        withContext(dispatcher) {
            localDataSource.clear()
        }
    }

    private fun joinProducts(productList: List<ProductInvoice>) : List<ProductInvoice> {
        return productList
            .groupBy { (it.product.eanCode ?: it.product.name) }
            .values
            .map { list ->
                ProductInvoice(
                    list[0].id,
                    list[0].product,
                    list[0].price,
                    list.sumOf { it.quantity },
                    list.sumOf { it.discount }
                )
            }
    }

    private fun joinProductInvoiceCompleteInfo(productList: List<ProductInvoiceCompleteInfo>) : List<ProductInvoiceCompleteInfo> {
        return productList
            .groupBy { (it.invoiceId) }
            .values
            .map { list ->
                ProductInvoiceCompleteInfo(
                    invoiceId = list[0].invoiceId,
                    date = list[0].date,
                    productInvoice = list[0].productInvoice.copy(
                        quantity = list.sumOf { it.productInvoice.quantity },
                        discount = list.sumOf { it.productInvoice.discount }
                    )
                )
            }
    }
}