package com.lucasxvirtual.financasmercado.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import com.lucasxvirtual.financasmercado.ui.invoice.InvoiceUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: Repository
) : ViewModel() {

    val uiState = repository.getInvoice(savedStateHandle.get<String>("invoiceId").orEmpty())
        .map { InvoiceUIState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InvoiceUIState()
        )
}