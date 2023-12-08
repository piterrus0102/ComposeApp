package com.example.composeapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = mainColor,
    secondary = textColor,
    tertiary = secondaryTextColor,
    surfaceTint = Color.Black,
    background = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = mainColor,
    secondary = textColor,
    tertiary = secondaryTextColor,
    surfaceTint = Color.White,
    background = Color.White
)

val standardTopRoundedCornerShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 4.dp
)

val standardBottomRoundedCornerShape = RoundedCornerShape(
    bottomStart = 4.dp,
    bottomEnd = 4.dp
)

val buttonRoundedCornerShape = RoundedCornerShape(
    size = 8.dp
)

@Composable
fun ComposeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (!darkTheme) {
            LightColorScheme
        } else {
            DarkColorScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}