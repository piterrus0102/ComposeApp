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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.example.composeapp.base.ui.theme.mainColor


@Composable
fun DialogRecorder(
    testName: String,
    state: DialogRecorderState,
    onPlayerButtonClicked: () -> Unit,
    onFailButtonClicked: () -> Unit,
    onPassButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Surface(shadowElevation = 10.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    TopBarTestName(testName = testName)
                    MicPlayer(
                        state = state,
                        onPlayerButtonClicked = onPlayerButtonClicked
                    )
                    BottomBarButtons(
                        onFailButtonClicked = onFailButtonClicked,
                        onPassButtonClicked = onPassButtonClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBarTestName(testName: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x7fd9d9dc))
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(all = 20.dp),
            text = testName
        )
    }
}

@Composable
private fun MicPlayer(
    state: DialogRecorderState,
    onPlayerButtonClicked: () -> Unit
) {
    val micTextStringId: Int
    val micImageId: Int
    when(state) {
        DialogRecorderState.ReadyForRecording -> {
            micTextStringId = R.string.ready_to_start
            micImageId = R.drawable.ic_microphone_white
        }
        DialogRecorderState.Recording -> {
            micTextStringId = R.string.recording
            micImageId = R.drawable.ic_stop_50dp
        }
        DialogRecorderState.ReadyForPlaying -> {
            micTextStringId = R.string.play_record
            micImageId = R.drawable.ic_play_50dp
        }

        DialogRecorderState.Playing -> {
            micTextStringId = R.string.play_record
            micImageId = R.drawable.ic_stop_50dp
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    vertical = 30.dp,
                    horizontal = 90.dp
                )
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(100.dp)
                    .height(100.dp)
                    .clickable {
                        onPlayerButtonClicked.invoke()
                    }

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(mainColor)
                        .padding(all = 30.dp)
                        .fillMaxSize()

                ) {
                    Image(
                        painter = painterResource(id = micImageId),
                        contentDescription = "microphone",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(id = micTextStringId)
            )

        }
    }

}

@Composable
fun BottomBarButtons(
    onFailButtonClicked: () -> Unit,
    onPassButtonClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x7fd9d9dc))
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Red),
            modifier = Modifier
                .padding(all = 20.dp)
                .weight(1F)
                .clickable { onFailButtonClicked.invoke() },
            text = "Fail",
            textAlign = TextAlign.Center
        )
        Text(
            style = MaterialTheme.typography.titleLarge.copy(color = mainColor),
            modifier = Modifier
                .padding(all = 20.dp)
                .weight(1F)
                .clickable { onPassButtonClicked.invoke() },
            text = "Pass",
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PiterrusAppTheme {
        DialogRecorder(
            testName = "CameraMic",
            state = DialogRecorderState.ReadyForRecording,
            onPlayerButtonClicked = {},
            onFailButtonClicked = {},
            onPassButtonClicked = {}
        )
    }
}