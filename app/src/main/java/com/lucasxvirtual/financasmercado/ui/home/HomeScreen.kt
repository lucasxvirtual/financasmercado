package com.lucasxvirtual.financasmercado.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation
import com.lucasxvirtual.financasmercado.data.model.MonthlyReport
import com.lucasxvirtual.financasmercado.data.model.SpentOnProductReport
import com.lucasxvirtual.financasmercado.extensions.monthDate
import com.lucasxvirtual.financasmercado.extensions.round
import com.lucasxvirtual.financasmercado.ui.atom.Ad
import com.lucasxvirtual.financasmercado.ui.atom.PrimaryButton
import com.lucasxvirtual.financasmercado.ui.clickableSingle
import com.lucasxvirtual.financasmercado.ui.molecule.EmptyPlaceholder
import com.lucasxvirtual.financasmercado.ui.molecule.HomeTopBar
import com.lucasxvirtual.financasmercado.ui.molecule.InvoicesList
import com.lucasxvirtual.financasmercado.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onInvoiceClicked: (String) -> Unit = {},
    onInvoiceItemClicked: (Int, String) -> Unit = { _, _ -> },
    onImportClicked: (Boolean) -> Unit = {},
    onMonthClicked: (String) -> Unit = {},
    onSettingsClicked: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState(HomeUIState.LastInvoice())
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                onSettingsPressed = onSettingsClicked
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        HomeScreen(
            uiState = uiState,
            onImportClicked = onImportClicked,
            onInvoiceItemClicked = onInvoiceItemClicked,
            onInvoiceClicked = onInvoiceClicked,
            onFilterItemClicked = viewModel::onFilterItemClicked,
            onMonthClicked = onMonthClicked,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeScreen(
    uiState: HomeUIState,
    modifier: Modifier = Modifier,
    onInvoiceClicked: (String) -> Unit = {},
    onInvoiceItemClicked: (Int, String) -> Unit = { _, _ -> },
    onImportClicked: (Boolean) -> Unit = {},
    onFilterItemClicked: (FilterItem) -> Unit = {},
    onMonthClicked: (String) -> Unit = {}
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onImportClicked(it)
    }

    Column(modifier = modifier) {
        val lazyListState = rememberLazyListState()

        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            itemsIndexed(uiState.items) { index, item ->
                val (backgroundColor, textColor) = if (uiState.selectedItem == item) {
                    MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.background to MaterialTheme.colorScheme.primary
                }
                val paddingStart = if (index == 0) 20.dp else 8.dp
                val paddingEnd = if (index == uiState.items.lastIndex) 20.dp else 8.dp
                Column(
                    modifier = Modifier
                        .padding(
                            start = paddingStart,
                            end = paddingEnd,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        )
                        .background(backgroundColor)
                        .clickableSingle {
//                            coroutineScope.launch {
//                                lazyListState.animateScrollToItem(index)
//                            }
                            onFilterItemClicked(item)
                        }
                ) {
                    Text(
                        text = stringResource(id = item.title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = textColor,
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                    )
                }
            }
        }

        when (uiState) {
            is HomeUIState.LastInvoice -> LastInvoice(
                lastInvoice = uiState.lastInvoice,
                onInvoiceItemClicked = onInvoiceItemClicked
            )
            is HomeUIState.ByInvoiceDate -> ByInvoiceDate(
                invoiceList = uiState.invoiceList,
                onInvoiceClicked = onInvoiceClicked
            )
            is HomeUIState.ByMonth -> ByMonth(
                monthlyReportList = uiState.monthlyReportList,
                onMonthClicked = onMonthClicked
            )
            is HomeUIState.BySpentOnProduct -> BySpentOnProduct(
                spentOnProductReportList = uiState.spentOnProductReportList,
                onProductClicked = onInvoiceItemClicked
            )
            is HomeUIState.Loading -> Loading(modifier = Modifier
                .weight(1f, true)
                .fillMaxWidth())
        }

        PrimaryButton(
            text = stringResource(R.string.import_invoice),
            action = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    onImportClicked(true)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
        Ad(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun ColumnScope.LastInvoice(
    lastInvoice: Invoice?,
    onInvoiceItemClicked: (Int, String) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.weight(1f)) {
        if (lastInvoice == null) {
            EmptyPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                InvoicesList(
                    invoice = lastInvoice,
                    onInvoiceItemClicked = onInvoiceItemClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ColumnScope.ByInvoiceDate(
    invoiceList: List<InvoiceSimpleInformation>,
    onInvoiceClicked: (String) -> Unit = {}
) {
    Column(modifier = Modifier.weight(1f)) {
        if (invoiceList.isEmpty()) {
            EmptyPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                InvoicesList(invoiceList = invoiceList, onInvoiceClicked = onInvoiceClicked)
            }
        }
    }
}

@Composable
fun ColumnScope.ByMonth(
    monthlyReportList: List<MonthlyReport>,
    onMonthClicked: (String) -> Unit
) {
    Column(modifier = Modifier.weight(1f)) {
        if (monthlyReportList.isEmpty()) {
            EmptyPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                LazyColumn {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.month),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(R.string.total_spent),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    itemsIndexed(monthlyReportList) { index, it ->
                        Card(
                            elevation = CardDefaults.cardElevation(0.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = if (index % 2 == 0)
                                CardDefaults.cardColors(Color.White)
                            else
                                CardDefaults.cardColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMonthClicked(it.date) }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = it.date.monthDate(),
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
                                        it.spent.round()
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.BySpentOnProduct(
    spentOnProductReportList: List<SpentOnProductReport>,
    onProductClicked: (Int, String) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.weight(1f)) {
        if (spentOnProductReportList.isEmpty()) {
            EmptyPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                LazyColumn {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.data_based_on_last_3_month),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 20.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.product),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(R.string.total_spent),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    itemsIndexed(spentOnProductReportList) { index, it ->
                        Card(
                            elevation = CardDefaults.cardElevation(0.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = if (index % 2 == 1)
                                CardDefaults.cardColors(Color.White)
                            else
                                CardDefaults.cardColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableSingle { onProductClicked(it.id, it.name) }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
                            ) {
                                Text(
                                    text = it.name,
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
                                        it.spent.round()
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}
