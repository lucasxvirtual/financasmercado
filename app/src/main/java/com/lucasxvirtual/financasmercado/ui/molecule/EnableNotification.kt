package com.lucasxvirtual.financasmercado.ui.molecule

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.extensions.checkNotificationEnabled
import com.lucasxvirtual.financasmercado.extensions.openNotificationSettings

@Composable
fun EnableNotification(
    notificationEnabled: Boolean = true,
    notificationStatus: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var checked by remember {
        mutableStateOf(notificationEnabled && context.checkNotificationEnabled())
    }
    val lifecycle by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.State.RESUMED) {
            checked = notificationEnabled && context.checkNotificationEnabled()
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        checked = it
    }
    Row(
        modifier = Modifier.padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.activate_notifications),
            textAlign = TextAlign.Start,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                if (it) {
                    if (!context.checkNotificationEnabled()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            context.openNotificationSettings()
                        }
                    } else {
                        checked = true
                    }
                } else {
                    checked = false
                }
                notificationStatus(checked)
            },
            colors = SwitchDefaults.colors(
                MaterialTheme.colorScheme.primary,
                Color.White,
                Color.White
            ),
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}