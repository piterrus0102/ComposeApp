package com.example.composeapp.base.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composeapp.R

@Composable
fun TopBarWithLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 100.dp,
                ),
            painter = painterResource(id = R.drawable.doom),
            contentDescription = "Doom",
            contentScale = ContentScale.Inside,
        )
    }
}
