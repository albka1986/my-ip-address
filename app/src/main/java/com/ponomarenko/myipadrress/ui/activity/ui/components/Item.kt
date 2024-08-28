package com.ponomarenko.myipadrress.ui.activity.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun Item(title: String, value: String) {

    val clipboard: ClipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable {
                copyToClipboard(title, value, clipboard)
            }
            .heightIn(min = 120.dp)
            .background(colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
            text = title,
            style = typography.labelLarge,
            color = colorScheme.onSecondary,
        )
        AutoScalableText(
            text = value,
            modifier = Modifier.padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            style = typography.displayMedium,
            color = colorScheme.onSecondary,
        )
    }
}

fun copyToClipboard(label: CharSequence, text: CharSequence, clipboard: ClipboardManager) {
    val clip: ClipData = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

@Composable
@ThemePreviews
fun ItemPreview() {
    Item(title = "Title", value = "Some big text is here, but it doesn't matter")
}