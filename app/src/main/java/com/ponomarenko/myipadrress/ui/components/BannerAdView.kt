package com.ponomarenko.myipadrress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ponomarenko.myipadrress.utils.ThemePreviews

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    val isInEditMode =false

//    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    //                    adUnitId = context.getString(R.string.banner_ad_unit_id)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(
                        AdRequest.Builder()
                            .build()
                    )
                }
            }
        )
    }
}

@ThemePreviews
@Composable
fun AdvertPreview() {
    BannerAdView()
}