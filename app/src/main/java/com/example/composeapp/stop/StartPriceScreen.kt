package com.example.composeapp.stop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapp.R
import com.example.composeapp.base.ui.StandardButton
import com.example.composeapp.base.ui.TopBarWithLogo
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.backgroundGrayColor
import com.example.composeapp.base.ui.theme.mySmallTextStyle
import com.example.composeapp.base.ui.theme.myTextStyle

@Composable
fun StartPriceScreen(
    onButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarWithLogo(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            imageModifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 100.dp,
                )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(backgroundGrayColor)
                .weight(10f)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_stop),
                contentDescription = "stop"
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            text = stringResource(R.string.lbl_bb_not_accepted_title),
            style = myTextStyle.copy(fontSize = 21.sp, color = MaterialTheme.colorScheme.secondary)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.lbl_bb_not_accepted_message),
            style = mySmallTextStyle.copy(color = MaterialTheme.colorScheme.tertiary)
        )

        StandardButton(
            painted = true,
            text = "OK",
            onClick = onButtonClicked,
            modifier = Modifier
                .width(150.dp)
                .weight(1f)
                .wrapContentHeight()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PiterrusAppTheme {
        StartPriceScreen(
            onButtonClicked = {}
        )
    }
}