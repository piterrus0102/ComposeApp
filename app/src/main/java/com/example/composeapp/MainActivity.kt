package com.example.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.navigation.PiterrusNavHost

/**
 * Единственное Activity (точка входа в приложение)
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PiterrusAppTheme {
                val navController = rememberNavController()
                PiterrusNavHost(navController)
            }
        }
    }
}

