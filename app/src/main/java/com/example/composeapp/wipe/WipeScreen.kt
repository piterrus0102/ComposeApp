package com.example.composeapp.wipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.composeapp.R
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.myRegularTextStyle

@Composable
fun WipeScreen(
    onButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "bg",
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier.clickable { onButtonClicked.invoke() },
            text = stringResource(R.string.ready_to_wipe),
            style = myRegularTextStyle.copy(fontSize = 18.sp, color = Color.White)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WipeScreen_Preview() {
    PiterrusAppTheme {
        WipeScreen(
            onButtonClicked = {}
        )
    }
}