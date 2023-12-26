package com.example.composeapp.final_price

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.test_core.data.TestResultValue

@Composable
fun FinalPriceScreen(
    navController: NavHostController,
    testResultValue: com.example.test_core.data.TestResultValue
) {
    FinalPriceView(
        result = testResultValue,
        onButtonClicked = {
            navController.popBackStack()
        }
    )
}