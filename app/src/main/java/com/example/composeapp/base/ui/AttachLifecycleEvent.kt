package com.example.composeapp.base.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AttachLifecycleEvent(
    onResumeCallback: (() -> Unit)? = null,
    onStopCallback: (() -> Unit)? = null,
    onDisposeCallback: (() -> Unit)? = null,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnResumeCallback by rememberUpdatedState(onResumeCallback)
    val currentOnStopCallback by rememberUpdatedState(onStopCallback)
    val currentOnDisposeCallback by rememberUpdatedState(onDisposeCallback)
    val scope = rememberCoroutineScope()
    DisposableEffect(lifecycleOwner) {
        val eventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    scope.launch(Dispatchers.Main) {
                        currentOnResumeCallback?.invoke()
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    scope.launch(Dispatchers.Main) {
                        currentOnStopCallback?.invoke()
                    }
                }

                else -> {
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(eventObserver)
            currentOnDisposeCallback?.invoke()
        }
    }
}