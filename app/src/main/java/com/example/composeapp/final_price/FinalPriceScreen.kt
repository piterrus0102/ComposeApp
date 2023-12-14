package com.example.composeapp.final_price

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.composeapp.base.data.TestResultValue

@Composable
fun FinalPriceScreen(
    navController: NavHostController,
    testResultValue: TestResultValue
) {
    FinalPriceView(
        result = testResultValue,
        onButtonClicked = {
            navController.popBackStack()
        }
    )
}