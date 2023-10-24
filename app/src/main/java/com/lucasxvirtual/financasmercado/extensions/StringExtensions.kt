package com.lucasxvirtual.financasmercado.extensions

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isValidInvoiceNumber(): Boolean {
    return try {
        if (length != 44)
            return false
        val nonVerifyDigitKey = substring(0, length - 1)
        var multiplier = 4
        var sum = 0
        nonVerifyDigitKey.asIterable().forEach {
            sum += it.digitToInt() * multiplier
            multiplier--
            if (multiplier < 2) {
                multiplier = 9
            }
        }
        val checkSum = if (sum % 11 in 0..1 ) {
            0
        } else {
            11 - (sum % 11)
        }
        last().digitToInt() == checkSum
    } catch (e: Exception) {
        false
    }
}

fun String.date(): Date? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    return tryCatch (doOnError = { null }) {
        sdf.parse(this)
    }
}

fun String.formatDate(pattern: String = "d/MMM 'às' HH'h'"): String {
    val date = date()
    val outSdf = SimpleDateFormat(pattern, Locale.getDefault())
    return date?.let { outSdf.format(it) }.orEmpty()
}

fun String.dayMonthDate(): String {
    return formatDate("dd/MM")
}

fun String.monthDate(): String {
    return formatDate("MMMM")
}
