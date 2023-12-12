package com.example.composeapp.components

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.nio.ShortBuffer


@Composable
fun DialogRecorder(
    testName: String,
    state: PlayerState,
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

private var mAudioBuffer: ShortArray? = null
private var mData: ShortArray? = null

private suspend fun startRecording() {
    val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    mAudioBuffer = ShortArray(bufferSize)
    mData = testMic(MediaRecorder.AudioSource.MIC)
}

@SuppressLint("MissingPermission")
private suspend fun testMic(input: Int): ShortArray? {
    var record: AudioRecord? = null
    val result =
        ShortBuffer.allocate(44100 * 5)
    try {
        record = AudioRecord(
            input,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            mAudioBuffer!!.size
        )
        if (record.recordingState != AudioRecord.STATE_INITIALIZED) return null
        val detector = SilenceDetector(44100, 1.0f, 750, 10)
        record.startRecording()
        while (true) {
            // read into proxy buffer for analysis (can read to result otherwise)
            val read = record.read(mAudioBuffer!!, 0, mAudioBuffer!!.size)
            if (read == AudioRecord.ERROR_INVALID_OPERATION || read == AudioRecord.ERROR_BAD_VALUE || read <= 0) {
                continue
            }
            if (detector.analyze(mAudioBuffer!!, read)) {
               // mHandler.sendEmptyMessage(com.nsysgroup.nsystest.ui.views.AdvancedMicTest.sMSG_SILENCE)
                continue
            }
            result.put(mAudioBuffer, 0, Math.min(read, result.remaining()))
            if (read > result.remaining()) break else {
                var level = Math.abs(mAudioBuffer!![0].toInt())
                var i = 1
                while (read / 4 > i) {
                    // sample only part of a buffer
                    level = Math.max(level, Math.abs(mAudioBuffer!![i].toInt()))
                    ++i
                }
//                Message.obtain(
//                    mHandler,
//                    com.nsysgroup.nsystest.ui.views.AdvancedMicTest.sMSG_PROGRESS,
//                    result.position(),
//                    level
//                ).sendToTarget()
            }
        }
        record.stop()
        return result.array()
    } catch (ex: Exception) {
        ex.printStackTrace()
    } finally {
        viewModel!!.mutableState.value = PlayerState.WaitingPlaying
        record?.release()
    }
    return null
}

private var mPlayer: AudioTrack? = null


private suspend fun togglePlayback() {
    if (null == mData || 0 == mData!!.size) return
    if (null != mPlayer && AudioTrack.PLAYSTATE_STOPPED != mPlayer?.playState) {
        viewModel!!.mutableState.value = PlayerState.Stopped
        stopPlayer()
        return
    }
    try {
        if (null != mPlayer) mPlayer!!.playbackHeadPosition = 0 else {
            mPlayer = AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                mData!!.size * 2,
                AudioTrack.MODE_STATIC
            ) // Write all the data at once
            mPlayer!!.write(mData!!, 0, mData!!.size)
            mPlayer!!.setNotificationMarkerPosition(mData!!.size)
        }
        mPlayer!!.play()
        viewModel!!.mutableState.value = PlayerState.Playing
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        mPlayer = null
    } catch (e: IllegalStateException) {
        e.printStackTrace()
        mPlayer = null
    }
}

fun onMarkerReached(track: AudioTrack?) {
    stopPlayer()
}

private fun stopPlayer() {
    if (null != mPlayer) {
        mPlayer!!.stop()
        viewModel!!.mutableState.value = PlayerState.Stopped
    }
}

class SilenceDetector(
    sampleRate: Int, time_sec: Float,
    private val mThreshold: Int, private val mPeakCountThreshold: Int
) {
    private val mSilentSamplesThreshold: Int
    private var mSilentSamples = 0

    init {
        mSilentSamplesThreshold = (sampleRate * time_sec).toInt()
    }

    fun analyze(buff: ShortArray, size: Int): Boolean {
        var peaks = 0
        var s = 0
        while (size != s && peaks < mPeakCountThreshold) {
            if (Math.abs(buff[s].toInt()) > mThreshold) ++peaks
            ++s
        }
        if (peaks >= mPeakCountThreshold) {
            mSilentSamples = 0
            return false
        }
        mSilentSamples += size
        return mSilentSamples > mSilentSamplesThreshold
    }
}


@Composable
private fun MicPlayer(
    state: PlayerState,
    onPlayerButtonClicked: () -> Unit
) {
    val micTextStringId: Int
    val micImageId: Int
    when(state) {
        PlayerState.WaitingRecord -> {
            micTextStringId = R.string.ready_to_start
            micImageId = R.drawable.ic_microphone_white
        }
        PlayerState.Recording -> {
            micTextStringId = R.string.recording
            micImageId = R.drawable.ic_stop_50dp
            LaunchedEffect(key1 = Unit, block = {
                startRecording()
            })

        }
        PlayerState.WaitingPlaying -> {
            micTextStringId = R.string.play_record
            micImageId = R.drawable.ic_play_50dp
        }

        PlayerState.Playing -> {
            micTextStringId = R.string.play_record
            micImageId = R.drawable.ic_stop_50dp
            LaunchedEffect(key1 = Unit, block = {
                togglePlayback()
            })
        }
        PlayerState.Stopped -> {
            micTextStringId = R.string.play_record
            micImageId = R.drawable.ic_play_50dp
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
                    .width(100.dp)
                    .height(100.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(mainColor)
                        .padding(all = 30.dp)
                        .fillMaxSize()
                        .clickable {
                            onPlayerButtonClicked.invoke()
                        }
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
private fun BottomBarButtons(
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

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PiterrusAppTheme {
        DialogRecorder(
            testName = "CameraMic",
            state = PlayerState.WaitingRecord,
            onPlayerButtonClicked = {},
            onFailButtonClicked = {},
            onPassButtonClicked = {}
        )
    }
}