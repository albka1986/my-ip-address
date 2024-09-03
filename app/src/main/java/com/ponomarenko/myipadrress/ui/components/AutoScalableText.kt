package com.ponomarenko.myipadrress.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun AutoScalableText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalTextStyle.current.color,
    style: TextStyle = LocalTextStyle.current,
    textSize: TextUnit = style.fontSize
) {

    var scalableTextSize: TextUnit by remember { mutableStateOf(textSize) }

    Text(
        modifier = modifier,
        text = text,
        overflow = TextOverflow.Visible,
        maxLines = 1,
        style = style.copy(fontSize = scalableTextSize),
        color = color,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow) {
                scalableTextSize *= 0.90f // The parameter affects the speed of scaling
            }
        }
    )
}