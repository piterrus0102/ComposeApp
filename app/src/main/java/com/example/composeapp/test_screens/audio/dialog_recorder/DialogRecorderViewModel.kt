package com.example.composeapp.test_screens.audio.dialog_recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DialogRecorderViewModel : ViewModel() {

    private val playerMutableState = MutableStateFlow<PlayerState>(PlayerState.Waiting)

    private val dialogRecorderMutableState =
        MutableStateFlow<DialogRecorderState>(DialogRecorderState.ReadyForRecording)
    private val dialogRecorderState = dialogRecorderMutableState.asStateFlow()

    private val recordHelper = com.example.feature_test_audio.RecordHelper { audioData ->
        playerMutableState.value = PlayerState.FinishRecording
        audioPlayerHelper.setData(audioData)
        changePlayerState()
    }

    private val audioPlayerHelper = com.example.feature_test_audio.AudioPlayerHelper {
        playerMutableState.value = PlayerState.Stopped
        changePlayerState()
    }

    fun changeDialogRecorderState() {
        when (dialogRecorderState.value) {
            DialogRecorderState.ReadyForRecording -> {
                playerMutableState.value = PlayerState.Recording
                changePlayerState()
            }

            DialogRecorderState.ReadyForPlaying -> {
                playerMutableState.value = PlayerState.Playing
                changePlayerState()
            }

            DialogRecorderState.Playing -> {
                playerMutableState.value = PlayerState.Paused
                changePlayerState()
            }

            else -> {}
        }
    }

    private fun changePlayerState() {
        when (playerMutableState.value) {
            PlayerState.Waiting -> {}
            PlayerState.Recording -> {
                viewModelScope.launch(Dispatchers.Main) {
                    recordHelper.startRecording()
                }
                dialogRecorderMutableState.value = DialogRecorderState.Recording
            }

            PlayerState.FinishRecording -> {
                dialogRecorderMutableState.value = DialogRecorderState.ReadyForPlaying
            }

            PlayerState.Playing -> {
                viewModelScope.launch(Dispatchers.Main) {
                    audioPlayerHelper.resumePlayer()
                }
                dialogRecorderMutableState.value = DialogRecorderState.Playing
            }

            PlayerState.Paused -> {
                audioPlayerHelper.pausePlayer()
                dialogRecorderMutableState.value = DialogRecorderState.ReadyForPlaying
            }

            PlayerState.Stopped -> {
                audioPlayerHelper.stopPlayer()
                dialogRecorderMutableState.value = DialogRecorderState.ReadyForPlaying
            }
        }
    }
}

sealed class DialogRecorderState {
    object ReadyForRecording : DialogRecorderState()
    object Recording : DialogRecorderState()
    object ReadyForPlaying : DialogRecorderState()
    object Playing : DialogRecorderState()
}

sealed class PlayerState {
    object Waiting : PlayerState()
    object Recording : PlayerState()
    object FinishRecording : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Stopped : PlayerState()
}