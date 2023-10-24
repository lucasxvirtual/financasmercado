package com.lucasxvirtual.financasmercado.data.remote

import com.lucasxvirtual.financasmercado.data.remote.response.BaseRemoteResponse
import com.lucasxvirtual.financasmercado.data.remote.response.SubmitInvoiceResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface FinancasMercadoApi {
    @POST("invoice")
    suspend fun submitInvoice(
        @Field("invoiceNumber") invoiceNumber: String
    ): Response<BaseRemoteResponse<SubmitInvoiceResponse>>
}