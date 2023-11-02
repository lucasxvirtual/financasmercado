package com.lucasxvirtual.financasmercado.ui.product

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.extensions.date
import com.lucasxvirtual.financasmercado.extensions.dayMonthDate
import com.lucasxvirtual.financasmercado.extensions.formatDate
import com.lucasxvirtual.financasmercado.extensions.formattedQuantity
import com.lucasxvirtual.financasmercado.ui.atom.Ad
import com.lucasxvirtual.financasmercado.ui.atom.DefaultCard
import com.lucasxvirtual.financasmercado.ui.clickableSingle
import com.lucasxvirtual.financasmercado.ui.molecule.EmptyLast3MonthsPlaceholder
import com.lucasxvirtual.financasmercado.ui.molecule.TopBar
import com.lucasxvirtual.financasmercado.ui.theme.Crimson
import com.lucasxvirtual.financasmercado.ui.theme.Green
import com.lucasxvirtual.financasmercado.viewmodels.ProductViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onInvoiceClicked: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.product),
                scrollBehavior = scrollBehavior,
                onBackPressed = onBackPressed
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        ProductScreen(
            uiState = uiState,
            onInvoiceClicked = onInvoiceClicked,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun ProductScreen(
    uiState: ProductUIState,
    onInvoiceClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = uiState.productName,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )
        when (uiState) {
            is ProductUIState.EmptyProductUIState -> EmptyLast3MonthsPlaceholder(modifier = Modifier.fillMaxSize())
            is ProductUIState.LoadingProductUIState -> Loading(Modifier.fillMaxSize())
            is ProductUIState.FilledProductUIState -> Filled(
                uiState,
                onInvoiceClicked
            )
        }
        Ad(Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun RowScope.PriceContainer(
    title: String,
    price: Double,
    date: String
) {
    DefaultCard(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1.0f)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.price_format, price),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = date.dayMonthDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ColumnScope.Filled(
    uiState: ProductUIState.FilledProductUIState,
    onInvoiceClicked: (String) -> Unit
) {
    val priceMostExpensive = uiState.mostExpensivePurchase.productInvoice.price
    val priceLeastExpensive = uiState.leastExpensivePurchase.productInvoice.price
    var bestPriceMarked by remember {
        mutableIntStateOf(-1)
    }
    var worstPriceMarked by remember {
        mutableIntStateOf(-1)
    }
    if (priceLeastExpensive == priceMostExpensive) {
        worstPriceMarked = -2
    }
    Text(
        text = stringResource(R.string.data_based_on_last_3_month),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .align(Alignment.End)
            .padding(bottom = 10.dp)
    )

    if (uiState.productInvoiceList.groupBy { it.productInvoice.price }.size > 1) {
        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            xValues = uiState.productInvoiceList.sortedBy { it.date.date() }.map { it.date },
            yValues = uiState.productInvoiceList.sortedBy { it.date.date() }.map { it.productInvoice.price },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PriceContainer(
                title = stringResource(R.string.best_price),
                price = priceLeastExpensive,
                date = uiState.leastExpensivePurchase.date
            )
            Spacer(modifier = Modifier.width(40.dp))
            PriceContainer(
                title = stringResource(R.string.worst_price),
                price = priceMostExpensive,
                date = uiState.mostExpensivePurchase.date
            )
        }
        val lastPrice = uiState.productInvoiceList.first().productInvoice.price
        val beforeLastPrice = uiState.productInvoiceList.first { it.productInvoice.price != lastPrice }.productInvoice.price
        val percentage = (lastPrice - beforeLastPrice) * 100 / beforeLastPrice
        Text(
            text = stringResource(R.string.price_float),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.percent_format, percentage),
            style = MaterialTheme.typography.headlineMedium,
            color = if (percentage > 0) Crimson else Green,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    val quantityText = uiState.totalQuantity.formattedQuantity(uiState.unityType)
    Text(
        text = stringResource(
            R.string.purchased_quantity_format,
            quantityText
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(
            R.string.total_spent_with_product_format,
            uiState.totalSpent
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
    DefaultCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.last_purchases),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.unit_value_no_slash),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.qtd).replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.total),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        repeat(
            uiState.productInvoiceList.size
        ) {
            val item = uiState.productInvoiceList[it]
            Card(
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(0.dp),
                colors = if (it % 2 == 1)
                    CardDefaults.cardColors(Color.White)
                else
                    CardDefaults.cardColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableSingle { onInvoiceClicked(item.invoiceId) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.date.dayMonthDate(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    val color = when {
                        item.productInvoice.price == priceLeastExpensive &&
                                (bestPriceMarked == -1 || bestPriceMarked == it) -> {
                            bestPriceMarked = it
                            Green
                        }

                        item.productInvoice.price == priceMostExpensive &&
                                (worstPriceMarked == -1 || worstPriceMarked == it) -> {
                            worstPriceMarked = it
                            Crimson
                        }

                        else -> MaterialTheme.colorScheme.primary
                    }
                    Text(
                        stringResource(
                            id = R.string.price_no_currency_format,
                            item.productInvoice.price
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    val quantity =
                        item.productInvoice.quantity.formattedQuantity(item.productInvoice.product.unitType)
                    Text(
                        quantity,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(
                            id = R.string.price_format,
                            item.totalPrice
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun Graph(
    modifier : Modifier,
    xValues: List<String>,
    yValues: List<Double>
) {
    val color = MaterialTheme.colorScheme.primary
    val colorInt = color.toArgb()
    val tertiaryColor = MaterialTheme.colorScheme.secondary

    val minX = xValues.minBy { it.date()?.time ?: 0 }
    val maxX = xValues.maxBy { it.date()?.time ?: 0 }
    val minXString = minX.formatDate("d/MM")
    val maxXString = maxX.formatDate("d/MM")

    val minY = yValues.min()
    val maxY = yValues.max()
    val minYString = stringResource(id = R.string.price_no_currency_format, minY)
    val maxYString = stringResource(id = R.string.price_no_currency_format, maxY)

    val yDifference = maxY - minY
    Canvas(
        modifier = modifier,
    ) {
        val textPaint = Paint().apply {
            this.color = colorInt
            this.textSize = 12.dp.toPx()
        }

        val padding = textPaint.measureText(maxYString) + 40
        val xStep = (size.width - padding * 2) / xValues.lastIndex

        drawContext.canvas.nativeCanvas.drawText(
            minXString,
            padding - (textPaint.measureText(minXString)/2),
            size.height,
            textPaint
        )
        drawContext.canvas.nativeCanvas.drawText(
            maxXString,
            size.width - (textPaint.measureText(maxXString)/2) - padding,
            size.height,
            textPaint
        )
        drawContext.canvas.nativeCanvas.drawText(
            minYString,
            0f,
            size.height - padding,
            textPaint
        )
        drawContext.canvas.nativeCanvas.drawText(
            maxYString,
            0f,
            padding,
            textPaint
        )

        val coordinates = mutableListOf<PointF>()
        for (i in yValues.indices) {
            val x1 = padding + xStep * i
            val y1 = ((size.height - padding * 2) * ((maxY - yValues[i]) / yDifference) + padding).toFloat()
            coordinates.add(PointF(x1, y1))
        }

        val controlPoints1 = mutableListOf<PointF>()
        val controlPoints2 = mutableListOf<PointF>()

        for (i in 1 until coordinates.size) {
            controlPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
            controlPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
        }

        val stroke = Path().apply {
            reset()
            moveTo(coordinates.first().x, coordinates.first().y)
            for (i in 0 until coordinates.size - 1) {
                cubicTo(
                    controlPoints1[i].x,controlPoints1[i].y,
                    controlPoints2[i].x,controlPoints2[i].y,
                    coordinates[i + 1].x,coordinates[i + 1].y
                )
            }
        }
        drawPath(
            stroke,
            color = tertiaryColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(stroke.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(size.width - padding, size.height - padding)
                lineTo(padding, size.height - padding)
                close()
            }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                listOf(
                    tertiaryColor,
                    Color.Transparent,
                ),
                endY = size.height - padding
            ),
        )

        for (coordinate in coordinates) {
            drawCircle(
                color = color,
                radius = 14f,
                center = Offset(coordinate.x, coordinate.y)
            )
        }
    }
}
