package com.lucasxvirtual.financasmercado.ui.importinvoice

import com.lucasxvirtual.financasmercado.R

sealed class ImportInvoiceUIState {
    object Default: ImportInvoiceUIState()
    object Loading: ImportInvoiceUIState()
    object Success: ImportInvoiceUIState()
    object Error: ImportInvoiceUIState()
    object Close: ImportInvoiceUIState()

    val text : Int?
        get() = when (this) {
            is Loading -> R.string.importing_invoice
            is Success -> R.string.importing_success
            is Error -> R.string.importing_error
            else -> null
        }

    val showDialog: Boolean
        get() = when(this) {
            is Loading,
            is Success,
            is Error -> true
            else -> false
        }
}
