package com.lucasxvirtual.financasmercado.data.remote

import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.toInvoice
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val financasMercadoApi: FinancasMercadoApi
) {
    suspend fun submitInvoice(invoiceNumber: String): Invoice {
        val response = financasMercadoApi.postInvoice(invoiceNumber)
        return response.body()?.payload?.invoiceResponse?.toInvoice() ?: throw Exception()
    }

    suspend fun postNotificationToken(token: String): Boolean {
        return financasMercadoApi.postNotificationToken(token).isSuccessful
    }
}