package com.lucasxvirtual.financasmercado.extensions

import com.lucasxvirtual.financasmercado.data.model.UseCaseResult

internal inline fun <T, R> T.tryCatch(
    doOnError: T.(Throwable) -> R = { throw it },
    retries: Invocations = Invocations.Once,
    block: T.() -> R,
): R {
    var currentTries = 0
    var lasException: Throwable
    do {
        try {
            return block()
        } catch (throwable: Throwable) {
            currentTries++
            lasException = throwable
        }
    } while (currentTries < retries.times)
    return doOnError(lasException)
}

internal inline fun <T, R> T.tryCatchResult(
    doOnError: T.(Throwable) -> R = { throw it },
    retries: Invocations = Invocations.Once,
    block: T.() -> R,
): UseCaseResult<R> {
    return tryCatch(
        doOnError = {
            try {
                UseCaseResult.Success(doOnError(it))
            } catch (throwable: Throwable) {
                UseCaseResult.Failed(throwable)
            }
        },
        retries = retries,
        block = {
            UseCaseResult.Success(block())
        },
    )
}

internal sealed class Invocations(val times: Long) {
    object Once : Invocations(1)
    object ServiceRetry : Invocations(3)
    class Times(times: Long) : Invocations(times)
}
