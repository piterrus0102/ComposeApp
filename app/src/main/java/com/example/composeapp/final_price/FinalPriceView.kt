package com.example.composeapp.final_price

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.test_core.data.TestResultValue
import com.example.composeapp.base.ui.TopBarWithLogo
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.mySmallTextStyle
import com.example.composeapp.components.RowTextBetweenText
import com.example.composeapp.final_price.components.OrderCreatedFrame
import com.example.composeapp.final_price.components.PriceCard

@Composable
fun FinalPriceView(
    result: com.example.test_core.data.TestResultValue,
    onButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        OrderCreatedFrame(
            phoneName = "Poco M3"
        )

        val price = when (result) {
            com.example.test_core.data.TestResultValue.PASSED -> "600.00 $"
            com.example.test_core.data.TestResultValue.FAILED -> "200.00 $"
            else -> "400.00 $"
        }
        PriceCard(
            priceText = price,
            onButtonClicked = onButtonClicked
        )

        Spacer(modifier = Modifier.height(12.dp))

        RowTextBetweenText(
            textLeft = stringResource(id = R.string.order_number),
            textRight = "26354"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Start,
            text = "Here is a custom instruction what to do next!",
            style = mySmallTextStyle.copy(color = MaterialTheme.colorScheme.tertiary)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun FinalPriceView_Preview() {
    PiterrusAppTheme {
        FinalPriceView(
            result = com.example.test_core.data.TestResultValue.PASSED,
            onButtonClicked = {}
        )
    }
}