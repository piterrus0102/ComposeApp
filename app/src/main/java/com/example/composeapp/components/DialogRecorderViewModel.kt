package com.example.composeapp.components

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class DialogRecorderViewModel: ViewModel() {

    val mutableState = MutableStateFlow<PlayerState>(PlayerState.WaitingRecord)

}

sealed class PlayerState {
    object WaitingRecord : PlayerState()
    object Recording : PlayerState()
    object WaitingPlaying : PlayerState()
    object Playing : PlayerState()
    object Stopped : PlayerState()
}