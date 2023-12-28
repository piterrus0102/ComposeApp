package com.example.composeapp.base.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.composeapp.base.theme.PiterrusAppTheme
import com.example.composeapp.base.theme.myTextStyle

@Composable
fun YesNoDialog(
    title: String,
    yesButtonTitle: String,
    noButtonTitle: String,
    onYes: () -> Unit,
    onNo: () -> Unit,
) {
    Dialog(
        onDismissRequest = onNo
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = title,
                    style = myTextStyle,
                    modifier = Modifier.padding(16.dp),
                )
                Row {
                    Spacer(modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth())
                    Row(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = onNo,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(noButtonTitle)
                        }
                        TextButton(
                            onClick = onYes,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(yesButtonTitle)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun YesNoDialog_Preview() {
    PiterrusAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            YesNoDialog(
                title = "Have you seen flash?",
                yesButtonTitle = "Yes",
                noButtonTitle = "No",
                onYes = { },
                onNo = { }
            )
        }
    }
}