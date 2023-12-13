package com.example.composeapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.myBoldTextStyle
import com.example.composeapp.base.ui.theme.myExtraBoldTextStyle
import com.example.composeapp.base.ui.theme.textColor

@Composable
fun PinComponent(
    titleStringId: Int,
    pinSize: Int,
    onPinEntered:(String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var pin by remember { mutableStateOf("") }
        Text(
            text = stringResource(id = titleStringId)
        )

        Text(
            text = pin.ifEmpty { "•".repeat(pinSize) },
            style = myExtraBoldTextStyle.copy(
                color = if(pin.isEmpty()) Color(0xff82848c) else Color.Black
            )
        )
        NumberKeyboard(
            onButtonPressed = {
                pin += it
                if(pin.length == pinSize) {
                    onPinEntered.invoke(pin)
                }
            }
        )
    }
}

@Composable
fun NumberKeyboard(onButtonPressed: (String) -> Unit) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "1",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "2",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "3",
                onButtonPressed = onButtonPressed
            )
        }
        Row {
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "4",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "5",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "6",
                onButtonPressed = onButtonPressed
            )
        }
        Row {
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "7",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "8",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "9",
                onButtonPressed = onButtonPressed
            )
        }
        Row {
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "0",
                onButtonPressed = onButtonPressed
            )
            KeyboardButton(
                modifier = Modifier.weight(.33F),
                textCypher = "",
                onButtonPressed = onButtonPressed
            )
        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier,
    textCypher: String,
    onButtonPressed: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(64.dp)
            .clickable {
                onButtonPressed.invoke(textCypher)
            }
    ) {
        Text(
            text = textCypher,
            style = myBoldTextStyle.copy(color = textColor)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview1() {
    PiterrusAppTheme {
        PinComponent(
            titleStringId = R.string.enter_your_code,
            pinSize = 5,
            onPinEntered = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}