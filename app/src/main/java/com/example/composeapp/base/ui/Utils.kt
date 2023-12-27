package com.example.composeapp.base.ui

import android.content.Context
import com.example.composeapp.R
import com.example.test_core.model.BaseTest
import java.io.File

fun Int.toMmSs(): String {
    val minutes = this / 60
    val seconds = this % 60
    val stringMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
    val stringSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
    return "$stringMinutes:$stringSeconds"
}

fun List<BaseTest>.hasNext(index: Int): Boolean = index <= this.lastIndex

fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
}