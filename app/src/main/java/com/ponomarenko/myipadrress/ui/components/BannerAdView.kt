package com.ponomarenko.myipadrress.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.utils.ThemePreviews

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = context.getString(R.string.banner_ad_unit_id)
                loadAd(
                    AdRequest.Builder()
                        .build()
                )
            }
        }
    )
}

@ThemePreviews
@Composable
fun AdvertPreview() {
    BannerAdView()
}