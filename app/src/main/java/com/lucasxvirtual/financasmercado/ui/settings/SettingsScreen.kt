package com.lucasxvirtual.financasmercado.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.ui.molecule.EnableNotification
import com.lucasxvirtual.financasmercado.ui.molecule.TopBar
import com.lucasxvirtual.financasmercado.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val notificationEnabled by viewModel.notificationEnabled.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.settings),
                onBackPressed = onBackPressed,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        SettingsScreen(
            notificationEnabled = notificationEnabled,
            onNotificationEnabledChanged = viewModel::onNotificationEnabledChanged,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun SettingsScreen(
    notificationEnabled: Boolean,
    onNotificationEnabledChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        EnableNotification(
            notificationEnabled = notificationEnabled,
            notificationStatus = onNotificationEnabledChanged
        )
    }
}