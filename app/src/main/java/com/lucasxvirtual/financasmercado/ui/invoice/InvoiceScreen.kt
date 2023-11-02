package com.lucasxvirtual.financasmercado.ui.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.ui.atom.Ad
import com.lucasxvirtual.financasmercado.ui.molecule.InvoicesList
import com.lucasxvirtual.financasmercado.ui.molecule.TopBar
import com.lucasxvirtual.financasmercado.viewmodels.InvoiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    viewModel: InvoiceViewModel = hiltViewModel(),
    onInvoiceItemClicked: (Int, String) -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.invoice),
                scrollBehavior = scrollBehavior,
                onBackPressed = onBackPressed
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        InvoiceScreen(
            uiState = uiState,
            onInvoiceItemClicked = onInvoiceItemClicked,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun InvoiceScreen(
    uiState: InvoiceUIState,
    onInvoiceItemClicked: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        uiState.invoice?.let {
            InvoicesList(
                invoice = it,
                onInvoiceItemClicked,
                modifier = Modifier.weight(1f)
            )
        }
        Ad(Modifier.align(Alignment.CenterHorizontally))
    }
}