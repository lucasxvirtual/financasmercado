package com.lucasxvirtual.financasmercado.data.remote

import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.toInvoice
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val financasMercadoApi: FinancasMercadoApi
) {
    suspend fun submitInvoice(invoiceNumber: String): Invoice {
        val response = financasMercadoApi.submitInvoice(invoiceNumber)
        return response.body()?.payload?.invoiceResponse?.toInvoice() ?: throw Exception()
    }
}