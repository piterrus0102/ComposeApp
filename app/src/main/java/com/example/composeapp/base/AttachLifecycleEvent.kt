package com.example.composeapp.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun AttachLifecycleEvent(
    onStartCallback: (() -> Unit)? = null,
    onResumeCallback: (() -> Unit)? = null,
    onStopCallback: (() -> Unit)? = null,
    onDestroyCallback: (() -> Unit)? = null,
    onDisposeCallback: (() -> Unit)? = null,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnStartCallback by rememberUpdatedState(onStartCallback)
    val currentOnResumeCallback by rememberUpdatedState(onResumeCallback)
    val currentOnStopCallback by rememberUpdatedState(onStopCallback)
    val currentOnDestroyCallback by rememberUpdatedState(onDestroyCallback)
    val currentOnDisposeCallback by rememberUpdatedState(onDisposeCallback)
    DisposableEffect(lifecycleOwner) {
        val eventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    currentOnStartCallback?.invoke()
                }

                Lifecycle.Event.ON_RESUME -> {
                    currentOnResumeCallback?.invoke()
                }

                Lifecycle.Event.ON_STOP -> {
                    currentOnStopCallback?.invoke()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    currentOnDestroyCallback?.invoke()
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