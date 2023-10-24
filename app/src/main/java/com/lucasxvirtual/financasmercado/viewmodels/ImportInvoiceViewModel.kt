package com.lucasxvirtual.financasmercado.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import com.lucasxvirtual.financasmercado.extensions.tryCatch
import com.lucasxvirtual.financasmercado.ui.importinvoice.ImportInvoiceUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportInvoiceViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<ImportInvoiceUIState>(ImportInvoiceUIState.Default)
    val uiState = _uiState.asStateFlow()

    fun submitAccessKey(accessKey: String) {
        viewModelScope.launch {
            _uiState.update {
                ImportInvoiceUIState.Loading
            }
            val result = tryCatch(
                doOnError = { null }
            ) {
                repository.submitInvoice(accessKey)
            }
            if (result == null) {
                _uiState.update { ImportInvoiceUIState.Error }
            } else {
                _uiState.update {
                    ImportInvoiceUIState.Success
                }
            }
        }
    }

    fun onDialogShown() {
        if (uiState.value is ImportInvoiceUIState.Success) {
            _uiState.update { ImportInvoiceUIState.Close }
        } else {
            _uiState.update { ImportInvoiceUIState.Default }
        }
    }

    fun submitAll(invoiceIds: List<String>?) {
        viewModelScope.launch {
            _uiState.update {
                ImportInvoiceUIState.Loading
            }
            val result = tryCatch(
                doOnError = { false }
            ) {
                invoiceIds?.let {
                    repository.submitAll(it)
                }
                true
            }
            if (result) {
                _uiState.update {
                    ImportInvoiceUIState.Success
                }
            } else {
                _uiState.update { ImportInvoiceUIState.Error }
            }
        }
    }
}