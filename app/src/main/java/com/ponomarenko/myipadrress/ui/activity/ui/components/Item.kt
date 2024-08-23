package com.ponomarenko.myipadrress.ui.activity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ponomarenko.myipadrress.ui.activity.ui.theme.bodyFontFamily
import com.ponomarenko.myipadrress.ui.activity.ui.theme.displayFontFamily
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun Item(title: String, value: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .background(colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
            fontFamily = bodyFontFamily,
            text = title,
            color = colorScheme.onSecondary,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier.padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            fontFamily = displayFontFamily,
            text = value,
            color = colorScheme.onSecondary,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@ThemePreviews
fun ItemPreview() {
    Item(title = "Title", value = "Value")
}