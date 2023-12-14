package com.example.composeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composeapp.base.ui.theme.myRegularTextStyle

@Composable
fun RowTextBetweenText(
    textLeft: String,
    textRight: String
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .background(Color(0xFFF8F8F8))
            .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = textLeft,
                style = myRegularTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = textRight,
                style = myRegularTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}