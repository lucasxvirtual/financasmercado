package com.lucasxvirtual.financasmercado.ui.molecule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation
import com.lucasxvirtual.financasmercado.extensions.date
import com.lucasxvirtual.financasmercado.extensions.formatDate
import com.lucasxvirtual.financasmercado.extensions.formattedQuantity
import com.lucasxvirtual.financasmercado.extensions.openAddressInMap
import com.lucasxvirtual.financasmercado.extensions.round
import com.lucasxvirtual.financasmercado.ui.clickableSingle
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InvoicesList(
    invoice: Invoice,
    onInvoiceItemClicked: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var width by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    val maxPrice = stringResource(
        id = R.string.price_no_currency_format,
        invoice.productInvoiceList.maxByOrNull { it.roundedTotalPrice }?.roundedTotalPrice ?: 0.0
    )
    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            Card(
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.date_format, invoice.date.formatDate()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.total_price_format, invoice.totalPrice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                    Text(
                        text = stringResource(
                            R.string.place,
                            invoice.market.address?.name.orEmpty()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .clickableSingle {
                                context.openAddressInMap(invoice.market.address?.formattedName.orEmpty())
                            }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.product),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1F)
                    )
                    Column(
                        modifier = Modifier.padding(end = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.unit_value),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.qtd),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.run {
                            if (width != 0.dp) {
                                width(width)
                            } else {
                                this
                            }
                        }
                    )
                }
            }
        }
        itemsIndexed(invoice.productInvoiceList) { index, it ->
            Card(
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(0.dp),
                colors = if (index % 2 == 1)
                    CardDefaults.cardColors(Color.White)
                else
                    CardDefaults.cardColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableSingle { onInvoiceItemClicked(it.product.id, it.product.name) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = it.product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 20.dp)
                            .padding(vertical = 16.dp)
                    )
                    Column(
                        modifier = Modifier.padding(end = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.price_no_currency_format, it.price) + "/",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = it.quantity.formattedQuantity(it.product.unitType),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    val price = stringResource(id = R.string.price_no_currency_format, it.roundedTotalPrice)
                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .onGloballyPositioned { lc ->
                                if (width == 0.dp) {
                                    width = with(density) {
                                        calculateWidth(
                                            price,
                                            maxPrice,
                                            lc.size.width.toFloat()
                                        ).toDp() + 5.dp
                                    }
                                }
                            }
                            .run {
                                if (width != 0.dp) {
                                    width(width)
                                } else {
                                    this
                                }
                            }
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InvoicesList(
    invoiceList: List<InvoiceSimpleInformation>,
    onInvoiceClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val week = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        calendar.time
    }
    LazyColumn(modifier = modifier) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.date),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.total_price),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        itemsIndexed(invoiceList) { index, it ->
            Card(
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(0.dp),
                colors = if (index % 2 == 0)
                    CardDefaults.cardColors(Color.White)
                else
                    CardDefaults.cardColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableSingle { onInvoiceClicked(it.id) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickableSingle { onInvoiceClicked(it.id) }
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    val format = if (week.time < (it.date.date()?.time ?: 0)) {
                        "EEEE 'às' HH'h'"
                    } else {
                        "d/MMM 'às' HH'h'"
                    }
                    Text(
                        text = it.date.formatDate(format),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 20.dp)
                    )
                    Text(
                        text = stringResource(
                            id = R.string.price_format,
                            it.totalPrice.round()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

private fun calculateWidth(baseText: String, maxText: String, baseTextWidth: Float): Float {
    val widthPerChar = baseTextWidth / baseText.length
    return maxText.length * widthPerChar
}