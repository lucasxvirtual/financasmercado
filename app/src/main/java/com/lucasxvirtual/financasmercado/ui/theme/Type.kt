package com.lucasxvirtual.financasmercado.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lucasxvirtual.financasmercado.R


internal val ProximaFontFamily = FontFamily(
    Font(R.font.proxima_light, FontWeight.Light),
    Font(R.font.proxima_regular, FontWeight.Normal),
    Font(R.font.proxima_semibold, FontWeight.SemiBold),
    Font(R.font.proxima_bold, FontWeight.Bold),
    Font(R.font.proxima_extrabold, FontWeight.ExtraBold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    labelSmall = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.15.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.15).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.15).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.15).sp
    ),
    displayMedium = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp
    ),
    displayLarge = TextStyle(
        fontFamily = ProximaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 60.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1).sp
    )
)
