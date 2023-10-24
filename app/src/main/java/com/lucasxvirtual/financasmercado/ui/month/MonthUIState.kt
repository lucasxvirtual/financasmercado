package com.lucasxvirtual.financasmercado.ui.month

import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation

data class MonthUIState(
    val title: String = "",
    val invoiceList: List<InvoiceSimpleInformation> = emptyList()
)
