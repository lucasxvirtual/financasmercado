package com.lucasxvirtual.financasmercado.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private const val TWO_DECIMALS_FORMAT = "#.##"
private const val DOT = '.'

internal fun Double.round(): Double {
    val currentLocale = Locale.getDefault()
    val decimalSeparator = DecimalFormatSymbols(currentLocale)
    decimalSeparator.decimalSeparator = DOT
    val decimalFormat = DecimalFormat(TWO_DECIMALS_FORMAT, decimalSeparator)
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(this).toDouble()
}

fun Double.formattedQuantity(unitType: String): String {
    return String.format(
        if (this % 1.0 != 0.0)
            "%.3f%s"
        else
            "%.0f%s", this, unitType
    )
}