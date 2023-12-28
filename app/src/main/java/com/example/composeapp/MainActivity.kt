package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.navigation.PiterrusNavHost
import org.koin.androidx.compose.koinViewModel

/**
 * Единственное Activity (точка входа в приложение)
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PiterrusAppTheme {
                val navController = rememberNavController()
                val testsViewModel: TestsViewModel = koinViewModel()
                PiterrusNavHost(
                    navController = navController,
                    viewModel = testsViewModel
                )
            }
        }
    }
}

