package com.example.composeapp.base.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.composeapp.ui.theme.buttonRoundedCornerShape
import com.example.composeapp.ui.theme.buttonTextStyle

@Composable
fun StandardButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        shape = buttonRoundedCornerShape,
        content = {
            Text(
                text = text,
                style = buttonTextStyle
            )
        }
    )
}