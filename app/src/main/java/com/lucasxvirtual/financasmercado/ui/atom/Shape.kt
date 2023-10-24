package com.lucasxvirtual.financasmercado.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp

@Composable
fun Line(modifier: Modifier) {
    Box(modifier = modifier.height(1.dp).background(Gray, RoundedCornerShape(4.dp)))
}