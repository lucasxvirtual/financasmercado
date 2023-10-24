package com.lucasxvirtual.financasmercado.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun Context.getAllInvoicesIds(): List<String>? {
    return assets.list("")?.filter { it.startsWith("submit") }?.map { it.split("-")[1].split(".")[0] }
}