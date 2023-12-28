package com.example.feature_test_camera.ui

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
fun DecoratedBarcodeView(
    context: Context,
    onScanResult: (String) -> Unit
) {
    val decoratedBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.resume()
            this.decodeContinuous(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    onScanResult.invoke(result?.result?.text ?: "")
                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
            })
        }
    }

    AndroidView(
        modifier = Modifier,
        factory = { decoratedBarcodeView },
    )
}