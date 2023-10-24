package com.lucasxvirtual.financasmercado.ui.home

import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation
import com.lucasxvirtual.financasmercado.data.model.MonthlyReport
import com.lucasxvirtual.financasmercado.data.model.SpentOnProductReport

sealed class HomeUIState(
    val items: List<FilterItem> = FilterItem.values().toList(),
    val selectedItem: FilterItem
) {
    class LastInvoice(
        val lastInvoice: Invoice? = null
    ): HomeUIState(selectedItem = FilterItem.LAST_INVOICE)
    class ByInvoiceDate(
        val invoiceList: List<InvoiceSimpleInformation> = emptyList()
    ): HomeUIState(selectedItem = FilterItem.BY_INVOICE_DATE)
    class ByMonth(
        val monthlyReportList: List<MonthlyReport> = emptyList()
    ): HomeUIState(selectedItem = FilterItem.BY_MONTH)
    class BySpentOnProduct(
        val spentOnProductReportList: List<SpentOnProductReport> = emptyList()
    ): HomeUIState(selectedItem = FilterItem.BY_SPENT_ON_PRODUCT)
    object Loading: HomeUIState(selectedItem = FilterItem.LAST_INVOICE)
}

enum class FilterItem(val title: Int) {
    LAST_INVOICE(R.string.last_invoice),
    BY_INVOICE_DATE(R.string.invoices),
    BY_MONTH(R.string.month),
    BY_SPENT_ON_PRODUCT(R.string.by_spent_on_product),
//    BY_PRODUCT_PRICE_FLOAT((R.string.by_product_price_float))
}
