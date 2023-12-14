package com.example.composeapp.android_managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun ChargeManager(
    onSystemEvent: (batteryLevel: Int, isCharging: Boolean) -> Unit
) {
    val context = LocalContext.current
    val currentOnSystemEvent = remember { onSystemEvent }
    DisposableEffect(context, Intent.ACTION_BATTERY_CHANGED) {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.extras?.let {
                    val batteryLevel = it.getInt(BatteryManager.EXTRA_LEVEL, -1)
                    val isCharging =
                        it.getInt(BatteryManager.EXTRA_PLUGGED, 0) != 0 ||
                                it.getInt(
                                    BatteryManager.EXTRA_STATUS,
                                    BatteryManager.BATTERY_STATUS_UNKNOWN
                                ) == BatteryManager.BATTERY_STATUS_CHARGING
                    currentOnSystemEvent(batteryLevel, isCharging)
                }
            }
        }
        context.registerReceiver(receiver, intentFilter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}