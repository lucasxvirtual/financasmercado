package com.lucasxvirtual.financasmercado.ui.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasxvirtual.financasmercado.ui.theme.FinancasMercadoTheme

internal val defaultRadius = 4.dp
internal val defaultHeight = 56.dp
internal val defaultStroke = 1.dp

@Composable
fun PrimaryButton(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = action,
        modifier = modifier.height(defaultHeight),
        shape = RoundedCornerShape(defaultRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        enabled = enabled,
        contentPadding = padding ?: ButtonDefaults.ContentPadding
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues? = null,
    enabled: Boolean = true,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    color: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedButton(
        text = text,
        action = action,
        padding = padding,
        enabled = enabled,
        borderColor = borderColor,
        color = color,
        modifier = modifier.height(defaultHeight)
    )
}

@Composable
internal fun OutlinedButton(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues? = null,
    enabled: Boolean = true,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    color: Color = MaterialTheme.colorScheme.primary
) {
    androidx.compose.material3.OutlinedButton(
        onClick = action,
        modifier = modifier,
        shape = RoundedCornerShape(defaultRadius),
        colors = ButtonDefaults.buttonColors(Color.Unspecified),
        border = BorderStroke(width = defaultStroke, borderColor),
        contentPadding = padding ?: ButtonDefaults.ContentPadding,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = color
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    FinancasMercadoTheme {
        PrimaryButton(
            text = "Primary Button",
            action = {},
            modifier = Modifier.width(300.dp)
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    FinancasMercadoTheme {
        SecondaryButton(
            text = "Secondary Button",
            action = {},
            modifier = Modifier.width(300.dp)
        )
    }
}
