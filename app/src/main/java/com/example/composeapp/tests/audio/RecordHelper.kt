package com.example.composeapp.tests.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.nio.ShortBuffer

class RecordHelper(
    val callback: (ShortArray) -> Unit
) {

    private var mAudioBuffer: ShortArray = shortArrayOf()

    suspend fun startRecording() {
        val bufferSize = AudioRecord.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        mAudioBuffer = ShortArray(bufferSize)
        testMic(MediaRecorder.AudioSource.MIC)
    }

    @SuppressLint("MissingPermission")
    private fun testMic(input: Int) {
        var record: AudioRecord? = null
        val result =
            ShortBuffer.allocate(44100 * 5)
        try {
            record = AudioRecord(
                input,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                mAudioBuffer.size
            )
            if (record.recordingState != AudioRecord.STATE_INITIALIZED) return
            val detector = SilenceDetector(44100, 1.0f, 750, 10)
            record.startRecording()
            while (true) {
                // read into proxy buffer for analysis (can read to result otherwise)
                val read = record.read(mAudioBuffer, 0, mAudioBuffer.size)
                if (read == AudioRecord.ERROR_INVALID_OPERATION || read == AudioRecord.ERROR_BAD_VALUE || read <= 0) {
                    continue
                }
                if (detector.analyze(mAudioBuffer, read)) {
                    // mHandler.sendEmptyMessage(com.nsysgroup.nsystest.ui.views.AdvancedMicTest.sMSG_SILENCE)
                    continue
                }
                result.put(mAudioBuffer, 0, Math.min(read, result.remaining()))
                if (read > result.remaining()) break else {
                    var level = Math.abs(mAudioBuffer[0].toInt())
                    var i = 1
                    while (read / 4 > i) {
                        // sample only part of a buffer
                        level = Math.max(level, Math.abs(mAudioBuffer[i].toInt()))
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
            callback(result.array())
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            record?.release()
        }
    }
}