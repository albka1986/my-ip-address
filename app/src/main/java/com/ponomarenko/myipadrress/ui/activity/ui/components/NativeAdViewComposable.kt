package com.ponomarenko.myipadrress.ui.activity.ui.components

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun NativeAdViewComposable() {
    val context = LocalContext.current
    val adUnitId = "ca-app-pub-3940256099942544/2247696110"
    //    val adUnitId = stringResource(id = R.string.banner_ad_unit_id)
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the ad load failure
                }
            })
            .build()

        adLoader.loadAd(
            AdRequest.Builder()
                .build()
        )

        onDispose {
            nativeAd?.destroy()
        }
    }

    nativeAd?.let { ad ->
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                val nativeAdView = FrameLayout(ctx)
                val adView = NativeAdView(ctx).apply {
                    // Bind native ad data to views
                    populateNativeAdView(ad, this)
                    val inflater = LayoutInflater.from(context)
                    val nativeView = inflater.inflate(R.layout.item, this, true)
            nativeView.nat
                    setNativeAd(nativeView)
                }
                nativeAdView.addView(adView)
                nativeAdView
            }
        )
    }
}


@ThemePreviews
@Composable
fun NativeAdViewComposablePreview() {
    NativeAdViewComposable()
}