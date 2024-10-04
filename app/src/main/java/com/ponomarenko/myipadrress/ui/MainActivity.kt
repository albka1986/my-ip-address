package com.ponomarenko.myipadrress.ui


import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.ponomarenko.myipadrress.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val viewModel: MainAndroidViewModel by inject<MainAndroidViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectUnsafeIntentLaunch()
                    .build()
            )
        }
        super.onCreate(savedInstanceState)

        Thread {
            MobileAds.initialize(this) { initializationStatus: InitializationStatus? ->
                Timber.d(initializationStatus.toString())
            }
        }.start()

        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.updateData()
    }
}