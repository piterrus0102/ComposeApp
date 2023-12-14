package com.example.composeapp.final_price.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapp.R
import com.example.composeapp.base.ui.theme.myTextStyle

@Composable
fun OrderCreatedFrame(
    phoneName: String
) {
    Column(
        modifier = Modifier
            .background(Color(0xffe0eef5))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 21.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = phoneName,
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
            style = myTextStyle.copy(
                fontSize = 21.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}