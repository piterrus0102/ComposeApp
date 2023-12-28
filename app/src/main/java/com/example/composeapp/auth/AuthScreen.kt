package com.example.composeapp.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.composeapp.base.ui_components.TopBarWithLogo
import com.example.composeapp.base.theme.PiterrusAppTheme
import com.example.composeapp.base.ui_components.PinComponent

@Composable
fun AuthScreen(
    onPinEntered: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBarWithLogo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 67.dp),
            imageModifier = Modifier.padding(horizontal = 53.dp)
        )
        PinComponent(
            titleStringId = R.string.enter_your_code,
            pinSize = 5,
            onPinEntered = onPinEntered,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = 30.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AuthScreen_Preview() {
    PiterrusAppTheme {
        AuthScreen(
            onPinEntered = {}
        )
    }
}