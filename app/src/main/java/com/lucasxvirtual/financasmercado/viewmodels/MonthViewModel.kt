package com.lucasxvirtual.financasmercado.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import com.lucasxvirtual.financasmercado.extensions.formatDate
import com.lucasxvirtual.financasmercado.ui.month.MonthUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MonthViewModel @Inject constructor(
    repository: Repository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val title: String = savedStateHandle.get<String>("date")?.formatDate("MMMM").orEmpty()

    val uiState = repository
        .getInvoicesFromMonth(savedStateHandle.get<String>("date")?.formatDate("Y-MM").orEmpty())
        .map {
            MonthUIState(
                title = title,
                invoiceList = it.reversed()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MonthUIState(title = title)
        )
}