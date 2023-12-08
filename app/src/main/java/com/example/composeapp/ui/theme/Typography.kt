package com.example.composeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.composeapp.R

private val light = Font(R.font.raleway_light, FontWeight.W300)
private val regular = Font(R.font.raleway_regular, FontWeight.W400)
private val medium = Font(R.font.raleway_medium, FontWeight.W500)
private val semibold = Font(R.font.raleway_semibold, FontWeight.W600)

private val craneFontFamily = FontFamily(fonts = listOf(light, regular, medium, semibold))


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        color = Color.Red,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

val mySmallTextStyle = TextStyle(
    fontFamily = craneFontFamily,
    fontSize = 14.sp,
    lineHeight = 20.4.sp,
    fontWeight = FontWeight(400),
)

val myTextStyle = TextStyle(
    fontFamily = craneFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 16.sp,
    lineHeight = 23.23.sp,
    textAlign = TextAlign.Center
)

val myRegularTextStyle = TextStyle(
    fontFamily = craneFontFamily,
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 23.23.sp,
    textAlign = TextAlign.Center
)

val buttonTextStyle = TextStyle(
    fontFamily = craneFontFamily,
    fontSize = 16.sp,
    lineHeight = 21.6.sp,
    fontWeight = FontWeight(400),
)

val myBoldTextStyle = TextStyle(
    fontFamily = craneFontFamily,
    fontSize = 32.sp,
    lineHeight = 46.46.sp,
    fontWeight = FontWeight(700),
    textAlign = TextAlign.Center,
)