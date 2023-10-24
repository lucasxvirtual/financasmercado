package com.lucasxvirtual.financasmercado.ui.month

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.ui.molecule.InvoiceList
import com.lucasxvirtual.financasmercado.ui.molecule.TopBar
import com.lucasxvirtual.financasmercado.viewmodels.MonthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(
    viewModel: MonthViewModel = hiltViewModel(),
    onInvoiceClicked: (String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopBar(
                title = uiState.title,
                scrollBehavior = scrollBehavior,
                onBackPressed = onBackPressed
            )
        }
    ) {
        MonthScreen(
            uiState = uiState,
            onInvoiceClicked = onInvoiceClicked,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun MonthScreen(
    uiState: MonthUIState,
    onInvoiceClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        InvoiceList(
            invoiceList = uiState.invoiceList,
            onInvoiceClicked = onInvoiceClicked,
            modifier = modifier
        )
    }
}