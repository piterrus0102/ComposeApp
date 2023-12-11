package com.example.composeapp.base.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composeapp.base.ui.theme.buttonRoundedCornerShape
import com.example.composeapp.base.ui.theme.buttonTextStyle
import com.example.composeapp.base.ui.theme.mainColor

@Composable
fun StandardButton(
    text: String,
    onClick: () -> Unit,
    painted: Boolean = false,
    modifier: Modifier = Modifier.padding()
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if(painted) mainColor else Color.Transparent,
            contentColor = if(painted) Color.White else mainColor
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