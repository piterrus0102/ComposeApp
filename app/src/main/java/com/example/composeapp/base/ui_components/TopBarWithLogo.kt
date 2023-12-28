package com.example.composeapp.base.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.composeapp.R

@Composable
fun TopBarWithLogo(modifier: Modifier, imageModifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            modifier = imageModifier,
            painter = painterResource(id = R.drawable.doom),
            contentDescription = "Doom",
            contentScale = ContentScale.Inside,
        )
    }
}
