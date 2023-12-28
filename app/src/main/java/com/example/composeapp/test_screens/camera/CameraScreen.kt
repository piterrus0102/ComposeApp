package com.example.composeapp.test_screens.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.test_core.data.TestResultValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onFinishTests: (results: Map<com.example.feature_test_camera.ICameraTest, TestResultValue>) -> Unit
) {
    val viewModel: CameraScreenViewModel = koinViewModel()
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraView(
            cameraState = viewModel.cameraState.collectAsState().value,
            onCameraAction = { viewModel.actionToMutation(it) },
            onFinishTests = { onFinishTests.invoke(viewModel.getTestsResults()) }
        )
    } else {
        LaunchedEffect(key1 = Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}