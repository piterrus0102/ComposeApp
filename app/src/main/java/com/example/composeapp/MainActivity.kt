package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.final_price.FinalPriceScreen
import com.example.composeapp.stop.StartPriceScreen
import com.example.composeapp.base.ui.theme.PiterrusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PiterrusAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.FinalPriceScreen.route) {
                    composable(Routes.FinalPriceScreen.route) {
                        //val mainViewModel = hiltViewModel<MainViewModel>()
                        FinalPriceScreen(
                            onButtonClicked = {
                                navController.navigate(Routes.StopScreen.route)
                            }
                        )
                    }
                    composable(Routes.StopScreen.route) {
//                        val parentEntry = remember(it) {
//                            navController.getBackStackEntry(Routes.Home.route)
//                        }
//                        val parentViewModel = hiltViewModel<MainViewModel>(
//                            parentEntry
//                        )
                        StartPriceScreen(onButtonClicked = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

sealed class Routes(val route: String) {
    object FinalPriceScreen : Routes("FinalPriceScreen")
    object StopScreen : Routes("StopScreen")
}


@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PiterrusAppTheme {
        FinalPriceScreen(
            onButtonClicked = {}
        )
    }
}