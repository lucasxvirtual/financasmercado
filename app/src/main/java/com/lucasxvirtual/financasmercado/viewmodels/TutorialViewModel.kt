package com.lucasxvirtual.financasmercado.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasxvirtual.financasmercado.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    fun onTutorialFinished(notificationChecked: Boolean) {
        viewModelScope.launch {
            repository.saveHasSeenTutorial()
            repository.saveNotificationEnabled(notificationChecked)
        }
    }
}