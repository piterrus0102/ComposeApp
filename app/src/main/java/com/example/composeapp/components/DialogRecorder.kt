package com.example.composeapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapp.R
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.myRegularTextStyle
import com.example.composeapp.base.ui.theme.myTextStyle

@Composable
fun DialogRecorder(
    onStartButtonClicked: () -> Unit,
    onFailButtonClicked: () -> Unit,
    onPassButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x7fd9d9dc))
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 20.dp),
                    text = "Camera mic"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x7fd9d9dc))
            ) {
                Spacer(Modifier.weight(1F))
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 20.dp).weight(1F),
                    text = "Fail"
                )
                Spacer(Modifier.weight(1F))
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 20.dp).weight(1F),
                    text = "Pass"
                )
                Spacer(Modifier.weight(1F))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PiterrusAppTheme {
        DialogRecorder(
            onStartButtonClicked = {},
            onFailButtonClicked = {},
            onPassButtonClicked = {}
        )
    }
}