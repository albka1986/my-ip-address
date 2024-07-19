package com.ponomarenko.myipadrress.ui.activity.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Light theme",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBF8FF
)
@Preview(
    name = "Dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF131318
)
annotation class ThemePreviews

@Preview(name = "Foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "Tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
annotation class DevicePreviews