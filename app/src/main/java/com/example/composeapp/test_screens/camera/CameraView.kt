package com.example.composeapp.test_screens.camera

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.composeapp.base.ui.AttachLifecycleEvent
import com.example.composeapp.base.ui.YesNoDialog
import com.example.composeapp.base.ui.getOutputDirectory
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import java.io.File
import java.util.concurrent.Executors


@Composable
fun CameraView(
    cameraState: CameraState,
    onTestPassed: () -> Unit,
    onTestFailed: () -> Unit,
    onPhotoCaptured: (Uri) -> Unit
) {
    when (val screenState = cameraState.screenState) {
        CameraScreenState.Initial -> {}
        is CameraScreenState.Execute -> {
            screenState.cameraTest?.let {
                ExecuteTestView(
                    cameraTest = it,
                    onPhotoCaptured = onPhotoCaptured,
                    onTestPassed = onTestPassed,
                    onTestFailed = onTestFailed
                )
            }
        }

        is CameraScreenState.PhotoCaptured -> {
            CapturedPhotoView(
                onTestPassed = onTestPassed,
                onTestFailed = onTestFailed,
                photoUri = screenState.uri
            )
        }
    }
}

@Composable
private fun ExecuteTestView(
    cameraTest: ICameraTest,
    onPhotoCaptured: (Uri) -> Unit,
    onTestPassed: () -> Unit,
    onTestFailed: () -> Unit
) {
    if (cameraTest is AutofocusBarcodeTest) {
        AutofocusBarcodeView(
            onTestPassed = onTestPassed,
            onTestFailed = onTestFailed
        )
    } else {
        CameraView(
            cameraTest = cameraTest,
            onPhotoCaptured = onPhotoCaptured
        )
    }
}

@Composable
fun AutofocusBarcodeView(
    onTestPassed: () -> Unit,
    onTestFailed: () -> Unit,
) {
    val context = LocalContext.current
    var barcodeResult by remember { mutableStateOf("") }
    if (barcodeResult.isNotEmpty()) {
        YesNoDialog(
            title = barcodeResult,
            yesButtonTitle = "Yes",
            noButtonTitle = "No",
            onYes = { onTestPassed.invoke() },
            onNo = { onTestFailed.invoke() }
        )
    }

    val decoratedBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.resume()
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    barcodeResult = result?.result?.text ?: ""
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

@Composable
fun CameraView(
    cameraTest: ICameraTest,
    onPhotoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    val flash = cameraTest.options.firstOrNull { it.name == "flash" && it.isInvolved } != null
    var photoCaptured by remember { mutableStateOf(Pair<Uri?, Boolean>(null, false)) }
    if (photoCaptured.second) {
        photoCaptured.first?.let {
            YesNoDialog(
                title = "Have you seen the flash?",
                yesButtonTitle = "Yes",
                noButtonTitle = "No",
                onYes = { onPhotoCaptured.invoke(it) },
                onNo = { onPhotoCaptured.invoke(it) }
            )
        }
    }
    HardCameraView(
        outputDirectory = context.getOutputDirectory(),
        onImageCaptured = {
            if (flash) {
                photoCaptured = Pair(it, true)
            } else {
                onPhotoCaptured.invoke(it)
            }
        },
        onError = {},
        cameraId = if (cameraTest is BackCameraTest) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        },
        isFlashModeOn = flash
    )
}

@Composable
private fun HardCameraView(
    outputDirectory: File,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    cameraId: Int,
    isFlashModeOn: Boolean
) {
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(if (isFlashModeOn) FLASH_MODE_ON else FLASH_MODE_OFF)
            .build()
    }
    val cameraSelector = remember { CameraSelector.Builder()
        .requireLensFacing(cameraId)
        .build() }
    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AttachLifecycleEvent(
        onDisposeCallback = {
            context.getCameraProvider().unbindAll() // this methods exists because of no call lifecycle callback on camera
        }
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                CameraAPI.takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = onImageCaptured,
                    onError = onError
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.AddCircle,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
    }
}

@Composable
private fun CapturedPhotoView(
    onTestPassed: () -> Unit,
    onTestFailed: () -> Unit,
    photoUri: Uri
) {
    Box(contentAlignment = Alignment.TopCenter) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(
                    modifier = Modifier
                        .weight(1F)
                        .padding(bottom = 20.dp),
                    onClick = onTestFailed,
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = "Close",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(1.dp)
                                .border(1.dp, Color.White, CircleShape)
                        )
                    }
                )
                IconButton(
                    modifier = Modifier
                        .weight(1F)
                        .padding(bottom = 20.dp),
                    onClick = onTestPassed,
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Done,
                            contentDescription = "Done",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(1.dp)
                                .border(1.dp, Color.White, CircleShape)
                        )
                    }
                )
            }
        }
    }
}

private fun Context.getCameraProvider(): ProcessCameraProvider =
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
                cameraProvider.get()
            },
            ContextCompat.getMainExecutor(this)
        )
    }.get()


//@androidx.compose.ui.tooling.preview.Preview
//@Composable
//fun CameraView_Preview() {
//    PiterrusAppTheme {
//        CameraView(
//            cameraState = CameraState(
//                screenState = CameraScreenState.Initial,
//            ),
//            onTestFailed = {},
//            onTestPassed = {},
//            onPhotoCaptured = {}
//        )
//    }
//}