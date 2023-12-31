package com.example.composeapp.base.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.composeapp.R

private val light = Font(R.font.roboto_light, FontWeight.W300)
private val regular = Font(R.font.roboto_regular, FontWeight.W400)
private val medium = Font(R.font.roboto_medium, FontWeight.W500)
private val semibold = Font(R.font.roboto_bold, FontWeight.W600)

private val piterrusFontFamily = FontFamily(fonts = listOf(light, regular, medium, semibold))

val dialogRecorderStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
)

val mySmallTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontSize = 14.sp,
    lineHeight = 20.4.sp,
    fontWeight = FontWeight(400),
)

val myTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 16.sp,
    lineHeight = 23.23.sp,
    textAlign = TextAlign.Center
)

val myRegularTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 23.23.sp,
    textAlign = TextAlign.Center
)

val buttonTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontSize = 16.sp,
    lineHeight = 21.6.sp,
    fontWeight = FontWeight(400),
)

val myBoldTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontSize = 32.sp,
    lineHeight = 46.46.sp,
    fontWeight = FontWeight(700),
    textAlign = TextAlign.Center,
)

val myExtraBoldTextStyle = TextStyle(
    fontFamily = piterrusFontFamily,
    fontSize = 65.sp,
    fontWeight = FontWeight(700),
    textAlign = TextAlign.Center,
)

val gray7C40016TextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 16.sp,
    fontFamily = piterrusFontFamily,
    fontWeight = FontWeight(400),
    color = Color(0xFF7C7C7C)
)

val grayA240016TextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 16.sp,
    fontFamily = piterrusFontFamily,
    fontWeight = FontWeight(400),
    color = Color(0xFFA2A2A2)
)