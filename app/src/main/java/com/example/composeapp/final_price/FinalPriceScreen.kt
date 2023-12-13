package com.example.composeapp.final_price

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapp.R
import com.example.composeapp.base.ui.StandardButton
import com.example.composeapp.base.ui.TopBarWithLogo
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.myBoldTextStyle
import com.example.composeapp.base.ui.theme.myRegularTextStyle
import com.example.composeapp.base.ui.theme.mySmallTextStyle
import com.example.composeapp.base.ui.theme.myTextStyle
import com.example.composeapp.base.ui.theme.standardRoundedCornerShape

@Composable
fun FinalPriceScreen(
    onButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBarWithLogo(
            modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
            imageModifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 100.dp,
                )
        )
        Column(
            modifier = Modifier
                .background(Color(0xffe0eef5))
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 21.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Poco M3",
                style = myTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.height(35.dp))

            val painter = painterResource(R.drawable.ic_hands_logo)
            Image(
                painter = painter,
                contentDescription = null,
                Modifier
                    .width(218.dp)
                    .height(76.dp)
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = stringResource(R.string.order_created),
                style = myTextStyle.copy(fontSize = 21.sp, color = MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.height(100.dp))

        }
        Layout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 24.dp, bottom = 4.dp),
            content = {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = standardRoundedCornerShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceTint
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp)
                    ) {
                        Text(
                            text = "Final price",
                            style = myRegularTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            text = "600.00 $",
                            style = myBoldTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        StandardButton(
                            text = stringResource(id = R.string.recalculate),
                            onClick = onButtonClicked
                        )
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                val column = measurables[0].measure(constraints)
                layout(column.width, column.height / 2){ //Change these per your needs
                    column.placeRelative(IntOffset(0, -column.height / 2))

                }
            }
        )


        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .background(Color(0xFFF8F8F8))
                .fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Order number",
                    style = myRegularTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                )
                Text(
                    text = "26354",
                    style = myRegularTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Start,
            text = "Here is a custom instruction what to do next!",
            style = mySmallTextStyle.copy(color = MaterialTheme.colorScheme.tertiary)
        )
    }
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