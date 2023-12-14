package com.example.composeapp.tests.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack


class AudioPlayerHelper(val onEndAudioData: () -> Unit) {
    private var audioTrack: AudioTrack? = null

    fun setData(audioData: ShortArray) {
        audioTrack = null
        configureAudioTrack(audioData)
    }

    private fun configureAudioTrack(audioData: ShortArray) {
        if (audioData.isEmpty()) {
            return
        }
        try {
            audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioData.size * 2,
                AudioTrack.MODE_STATIC
            ) // Write all the data at once
            audioTrack?.write(audioData, 0, audioData.size)
            audioTrack?.notificationMarkerPosition = audioData.size
            audioTrack?.setPlaybackPositionUpdateListener(object :
                AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onPeriodicNotification(track: AudioTrack?) {}

                override fun onMarkerReached(track: AudioTrack?) {
                    onEndAudioData.invoke()
                }
            })
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            audioTrack = null
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            audioTrack = null
        }
    }

    fun resumePlayer() {
        audioTrack?.play()
    }

    fun pausePlayer() {
        audioTrack?.pause()
    }

    fun stopPlayer() {
        audioTrack?.stop()
    }
}