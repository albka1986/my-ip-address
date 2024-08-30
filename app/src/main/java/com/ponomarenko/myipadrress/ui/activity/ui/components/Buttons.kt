package com.ponomarenko.myipadrress.ui.activity.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ponomarenko.myipadrress.ui.activity.ui.theme.AppTheme
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun PrimaryButton(onClick: () -> Unit, text: String, isLoading: Boolean = false) {
    Box {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(12.dp)
                .heightIn(min = 56.dp)
                .fillMaxWidth()

        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
@ThemePreviews
fun PrimaryButtonPreview() {
    AppTheme {
        PrimaryButton(onClick = {}, text = "Button")
    }
}