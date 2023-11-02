package com.lucasxvirtual.financasmercado.ui.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.lucasxvirtual.financasmercado.R

@Composable
fun Ad(modifier: Modifier = Modifier) {
    AndroidView(
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = it.getString(R.string.ad_unit_id)
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier
    )
}