package com.example.composeapp.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.tests.audio.AudioPlayerHelper
import com.example.composeapp.tests.audio.RecordHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DialogRecorderViewModel: ViewModel() {

    private val playerMutableState = MutableStateFlow<PlayerState>(PlayerState.Waiting)

    private val dialogRecorderMutableState = MutableStateFlow<DialogRecorderState>(DialogRecorderState.ReadyForRecording)
    val dialogRecorderState = dialogRecorderMutableState.asStateFlow()

    private val recordHelper = RecordHelper { audioData ->
        playerMutableState.value = PlayerState.FinishRecording
        audioPlayerHelper.setData(audioData)
        changePlayerState()
    }

    private val audioPlayerHelper = AudioPlayerHelper {
        playerMutableState.value = PlayerState.Stopped
        changePlayerState()
    }

    fun changeDialogRecorderState() {
        when(dialogRecorderState.value) {
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
        when(playerMutableState.value) {
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