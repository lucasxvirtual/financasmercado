package com.lucasxvirtual.financasmercado.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import com.lucasxvirtual.financasmercado.ui.home.FilterItem
import com.lucasxvirtual.financasmercado.ui.home.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    repository: Repository
): ViewModel() {

    private val lastInvoice: StateFlow<HomeUIState> = repository
        .getLastInvoice()
        .map { HomeUIState.LastInvoice(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState.Loading
        )

    private val byInvoiceDate: StateFlow<HomeUIState> = repository.getLastInvoices()
        .map { HomeUIState.ByInvoiceDate(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState.Loading
        )

    private val byMonth: StateFlow<HomeUIState> = repository.getMonthlyReport()
        .map { HomeUIState.ByMonth(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState.Loading
        )

    private val bySpentOnProduct: StateFlow<HomeUIState> = repository
        .getMostSpentProducts()
        .map { HomeUIState.BySpentOnProduct(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState.Loading
        )

    private val currentlySelected = MutableStateFlow(FilterItem.LAST_INVOICE)

    val uiState = combine(
        lastInvoice,
        byInvoiceDate,
        byMonth,
        bySpentOnProduct,
        currentlySelected
    ) { lastInvoice, byInvoiceDate, byMonth, bySpentOnProduct, filterItem ->
        when (filterItem) {
            FilterItem.LAST_INVOICE -> lastInvoice
            FilterItem.BY_INVOICE_DATE -> byInvoiceDate
            FilterItem.BY_MONTH -> byMonth
            FilterItem.BY_SPENT_ON_PRODUCT -> bySpentOnProduct
        }
    }

    fun onFilterItemClicked(filterItem: FilterItem) {
        currentlySelected.update { filterItem }
    }
}