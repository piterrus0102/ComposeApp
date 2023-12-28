package com.example.composeapp.test_screens.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.composeapp.base.getOutputDirectory
import com.example.composeapp.base.ui_components.YesNoDialog
import com.example.feature_test_camera.BackCameraTest
import com.example.feature_test_camera.ICameraTest
import com.example.feature_test_camera.ui.DecoratedBarcodeView
import com.example.feature_test_camera.ui.HardCameraView


@Composable
fun CameraView(
    cameraState: CameraState,
    onCameraAction: (CameraAction) -> Unit,
    onFinishTests: () -> Unit
) {
    when (val screenState = cameraState.screenState) {
        CameraScreenState.Initial -> {}
        is CameraScreenState.Execute -> {
            screenState.cameraTest?.let {
                ExecuteTestView(
                    cameraTest = it,
                    onCameraAction = onCameraAction
                )
            }
        }

        is CameraScreenState.PhotoCaptured -> {
            CapturedPhotoView(
                onTestPassed = { onCameraAction.invoke(CameraAction.TestResultAction(isPassed = true)) },
                onTestFailed = { onCameraAction.invoke(CameraAction.TestResultAction(isPassed = false)) },
                photoUri = screenState.uri
            )
        }

        is CameraScreenState.FinishTests -> {
            LaunchedEffect(key1 = Unit) {
                onFinishTests.invoke()
            }
        }
    }

}

@Composable
private fun ExecuteTestView(
    cameraTest: com.example.feature_test_camera.ICameraTest,
    onCameraAction: (CameraAction) -> Unit
) {
    if (cameraTest is com.example.feature_test_camera.AutofocusBarcodeTest) {
        AutofocusBarcodeView(
            onCameraAction = onCameraAction
        )
    } else {
        CameraView(
            cameraTest = cameraTest,
            onCameraAction = onCameraAction
        )
    }
}

@Composable
fun AutofocusBarcodeView(
    onCameraAction: (CameraAction) -> Unit
) {
    val context = LocalContext.current
    var barcodeResult by remember { mutableStateOf("") }
    if (barcodeResult.isNotEmpty()) {
        YesNoDialog(
            title = barcodeResult,
            yesButtonTitle = "Yes",
            noButtonTitle = "No",
            onYes = { onCameraAction.invoke(CameraAction.TestResultAction(isPassed = true)) },
            onNo = { onCameraAction.invoke(CameraAction.TestResultAction(isPassed = false)) }
        )
    }

    DecoratedBarcodeView(
        context = context,
        onScanResult = { barcodeResult = it }
    )
}



@Composable
fun CameraView(
    cameraTest: ICameraTest,
    onCameraAction: (CameraAction) -> Unit
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
                onYes = { onCameraAction.invoke(CameraAction.PhotoCapturedAction(it)) },
                onNo = { onCameraAction.invoke(CameraAction.PhotoCapturedAction(it)) }
            )
        }
    }
    HardCameraView(
        outputDirectory = context.getOutputDirectory(),
        onImageCaptured = {
            if (flash) {
                photoCaptured = Pair(it, true)
            } else {
                onCameraAction.invoke(CameraAction.PhotoCapturedAction(it))
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
