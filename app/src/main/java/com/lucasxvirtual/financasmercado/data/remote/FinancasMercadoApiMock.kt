package com.lucasxvirtual.financasmercado.data.remote

import android.content.Context
import com.lucasxvirtual.financasmercado.Constants
import com.lucasxvirtual.financasmercado.data.remote.response.BaseRemoteResponse
import com.lucasxvirtual.financasmercado.data.remote.response.SubmitInvoiceResponse
import com.lucasxvirtual.financasmercado.extensions.assetToObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.seconds

class FinancasMercadoApiMock @Inject constructor(
    private val context: Context,
    @Named(Constants.IO) private val dispatcher: CoroutineDispatcher
): FinancasMercadoApi {

    private val serviceDelay = 1.5.seconds

    override suspend fun postInvoice(invoiceNumber: String): Response<BaseRemoteResponse<SubmitInvoiceResponse>> {
        return withContext(dispatcher) {
            delay(serviceDelay)
            Response.success(context.assetToObject(MockAssets.SubmitInvoice(invoiceNumber)))
        }
    }

    override suspend fun postNotificationToken(token: String): Response<Unit> {
        return withContext(dispatcher) {
            Response.success(Unit)
        }
    }

    sealed class MockAssets(val fileName: String) {
        class SubmitInvoice(
            accessKey: String
        ) : MockAssets("submitInvoice-$accessKey.json")
    }
}