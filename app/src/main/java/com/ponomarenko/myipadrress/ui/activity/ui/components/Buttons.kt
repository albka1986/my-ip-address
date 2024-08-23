package com.ponomarenko.myipadrress.ui.activity.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ponomarenko.myipadrress.ui.activity.ui.theme.AppTheme
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun PrimaryButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(min = 56.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
        Text(text = text)
    }
}

@Composable
@ThemePreviews
fun PrimaryButtonPreview() {
    AppTheme {
        PrimaryButton(onClick = {}, text = "Button")
    }
}