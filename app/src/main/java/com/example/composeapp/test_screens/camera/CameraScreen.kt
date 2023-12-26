package com.example.composeapp.test_screens.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.composeapp.base.ui.AttachLifecycleEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavHostController
) {
    val viewModel: CameraScreenViewModel = koinViewModel()
    AttachLifecycleEvent()

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraView(
            cameraState = viewModel.cameraState.collectAsState().value,
            onTestPassed = {
                viewModel.saveTestResultValue(isPassed = true)
            },
            onTestFailed = {
                viewModel.saveTestResultValue(isPassed = false)
            },
            onPhotoCaptured = {
                viewModel.onPhotoCaptured(it)
            }
        )
    } else {
        LaunchedEffect(key1 = Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}