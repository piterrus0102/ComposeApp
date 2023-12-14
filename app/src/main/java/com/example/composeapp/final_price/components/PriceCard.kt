package com.example.composeapp.final_price.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.composeapp.base.ui.StandardButton
import com.example.composeapp.base.ui.theme.myBoldTextStyle
import com.example.composeapp.base.ui.theme.myRegularTextStyle
import com.example.composeapp.base.ui.theme.standardRoundedCornerShape


@Composable
fun PriceCard(
    priceText: String,
    onButtonClicked: () -> Unit
) {
    Layout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp, bottom = 4.dp),
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
                        text = priceText,
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
            layout(column.width, column.height / 2) {
                column.placeRelative(IntOffset(0, -column.height / 2))

            }
        }
    )
}