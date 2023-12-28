package com.example.composeapp.final_price

import androidx.compose.runtime.Composable

@Composable
fun FinalPriceScreen(
    onButtonClicked: () -> Unit,
    testResultValue: com.example.test_core.data.TestResultValue
) {
    FinalPriceView(
        result = testResultValue,
        onButtonClicked = onButtonClicked
    )
}