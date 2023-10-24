package com.lucasxvirtual.financasmercado.data.remote.response

import com.google.gson.annotations.SerializedName

data class BaseRemoteResponse<T>(
    @SerializedName("status") val status: String? = null,
    @SerializedName("payload") val payload: T? = null,
    @SerializedName("errors") val errors: List<BaseRemoteErrorResponse>? = null
) {
    fun isSuccessful(): Boolean {
        return status?.toIntOrNull()?.let {
            it in 200..300
        } ?: false
    }
}

data class BaseRemoteErrorResponse(
    @SerializedName("errorCode") val errorCode: String? = null,
    @SerializedName("message") val message: String?
)
