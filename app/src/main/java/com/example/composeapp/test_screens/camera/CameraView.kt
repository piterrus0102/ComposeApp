package com.example.composeapp.test_screens.camera

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.composeapp.R
import com.example.composeapp.base.ui.AttachLifecycleEvent
import com.example.composeapp.base.ui.YesNoDialog
import com.example.composeapp.base.ui.theme.PiterrusAppTheme
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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
                    onPhotoCaptured = onPhotoCaptured
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
    onPhotoCaptured: (Uri) -> Unit
) {
    if(cameraTest is AutofocusBarcodeTest) {
        AutofocusBarcodeView()
    } else {
        val context = LocalContext.current
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val flash = cameraTest.options.firstOrNull { it.name == "flash" && it.isInvolved } != null
        var photoCaptured by remember { mutableStateOf(Pair<Uri?, Boolean>(null, false)) }
        AttachLifecycleEvent(
            onDestroyCallback = {
                cameraExecutor.shutdown()
            }
        )
        if(photoCaptured.second) {
            photoCaptured.first?.let {
                YesNoDialog(
                    title = "Have you seen the flash?",
                    yesButtonTitle = "Yes",
                    noButtonTitle = "No",
                    onYes = {
                        onPhotoCaptured.invoke(it)
                    },
                    onNo = {
                        onPhotoCaptured.invoke(it)
                    }
                )
            }
        }
        HardCameraView(
            outputDirectory = context.getOutputDirectory(),
            executor = cameraExecutor,
            onImageCaptured = {
                if(flash) {
                    photoCaptured = Pair(it, true)
                } else {
                    onPhotoCaptured.invoke(it)
                }
            },
            onError = { Log.e("kilo", "View error:", it) },
            cameraId = if (cameraTest is BackCameraTest) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            },
            isFlashModeOn = flash
        )
    }
}

@Composable
fun AutofocusBarcodeView() {
    var barcodeResult by remember {
        mutableStateOf("")
    }

    if(barcodeResult.isNotEmpty()) {
        YesNoDialog(
            title = barcodeResult,
            yesButtonTitle = "Yes",
            noButtonTitle = "No",
            onYes = {

            },
            onNo = {

            }
        )
    }

    AndroidView(
        modifier = Modifier,
        factory = { context ->
            CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous(object : BarcodeCallback{
                override fun barcodeResult(result: BarcodeResult?) {
                    barcodeResult = result?.result?.text ?: ""
                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}

            })
            this.resume()
        } },
    )
}

@Composable
private fun HardCameraView(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    cameraId: Int,
    isFlashModeOn: Boolean
) {
    val lensFacing = remember { mutableIntStateOf(cameraId) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(if(isFlashModeOn) FLASH_MODE_ON else FLASH_MODE_OFF)
            .build()
    }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing.intValue)
        .build()

    LaunchedEffect(lensFacing) {
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
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = executor,
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

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener(
                {
                    continuation.resume(cameraProvider.get())
                },
                ContextCompat.getMainExecutor(this)
            )
        }
    }

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
}



@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraView_Preview() {
    PiterrusAppTheme {
        CameraView(
            cameraState = CameraState(
                screenState = CameraScreenState.Initial,
            ),
            onTestFailed = {},
            onTestPassed = {},
            onPhotoCaptured = {}
        )
    }
}
