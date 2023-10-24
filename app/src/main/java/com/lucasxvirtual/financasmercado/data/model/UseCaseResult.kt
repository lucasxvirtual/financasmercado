package com.lucasxvirtual.financasmercado.data.model

sealed class UseCaseResult<T> {
    data class Success<T>(val value: T) : UseCaseResult<T>()
    data class Failed<T>(val error: Throwable) : UseCaseResult<T>()
}