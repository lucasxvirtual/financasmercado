package com.lucasxvirtual.financasmercado.viewmodels

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import com.lucasxvirtual.financasmercado.ui.product.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: Repository
) : ViewModel(){

    private val productName: String = Uri.decode(
        savedStateHandle.get<String>("productName").orEmpty()
    )

    val uiState = repository.getProductCompleteInfo(savedStateHandle.get<Int>("productId") ?: 0)
        .map {
            if (it.isEmpty()) {
                ProductUIState.EmptyProductUIState(productName)
            } else {
                ProductUIState.FilledProductUIState(productName, it)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductUIState.LoadingProductUIState(productName)
        )
}
