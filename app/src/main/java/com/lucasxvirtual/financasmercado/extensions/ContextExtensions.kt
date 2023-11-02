package com.lucasxvirtual.financasmercado.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.lucasxvirtual.financasmercado.data.remote.FinancasMercadoApiMock


internal inline fun <reified T> Context.assetToObject(asset: FinancasMercadoApiMock.MockAssets): T {
    val gson = GsonBuilder().create()
    val mock = assets.open(asset.fileName).bufferedReader().use { it.readText() }
    return gson.fromJson(mock, object : TypeToken<T>() {}.type)
}

fun Context.openAddressInMap(address: String) {
    if (address.isEmpty()){
        return
    }
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("geo:0,0?q=$address")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.openNotificationSettings() {
    val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    startActivity(settingsIntent)
}

fun Context.getAllInvoicesIds(): List<String>? {
    return assets.list("")?.filter { it.startsWith("submit") }?.map { it.split("-")[1].split(".")[0] }
}

fun Context.checkNotificationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        NotificationManagerCompat.from(this).areNotificationsEnabled()
    }
}