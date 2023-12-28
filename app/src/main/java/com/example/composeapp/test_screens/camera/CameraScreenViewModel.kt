package com.example.composeapp.test_screens.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.composeapp.base.ui.hasNext
import com.example.test_core.data.TestResultValue
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraScreenViewModel : ViewModel() {

    // состояние экрана, сопровождающего тест
    private val cameraMutableState = MutableStateFlow(CameraState())
    val cameraState = cameraMutableState.asStateFlow()

    private val testsList = immutableListOf<com.example.feature_test_camera.ICameraTest>(
        com.example.feature_test_camera.BackCameraTest(),
        com.example.feature_test_camera.FrontCameraTest(),
        com.example.feature_test_camera.AutofocusBarcodeTest()
    )
    private var testsListIndex = 0
    private val testsResults = mutableMapOf<com.example.feature_test_camera.ICameraTest, TestResultValue>()
    fun getTestsResults() = testsResults.toImmutableMap()


    init {
        cameraMutableState.value = cameraState.value.copy(
            screenState = CameraScreenState.Execute(
                testIndex = testsListIndex,
                cameraTest = testsList[testsListIndex],
                readyForTest = true
            ),
        )
    }

    fun actionToMutation(cameraAction: CameraAction) {
        when (cameraAction) {
            is CameraAction.TestResultAction -> {
                testsResults[testsList[testsListIndex]] =
                    if (cameraAction.isPassed) TestResultValue.PASSED else TestResultValue.FAILED
                runNextText()
            }

            is CameraAction.PhotoCapturedAction -> {
                cameraMutableState.value = cameraState.value.copy(
                    screenState = CameraScreenState.PhotoCaptured(cameraAction.uri)
                )
            }
        }
    }

    private fun runNextText() {
        if (testsList.hasNext(testsListIndex + 1)) {
            testsListIndex++
            cameraMutableState.value = cameraState.value.copy(
                screenState = CameraScreenState.Execute(
                    testIndex = testsListIndex,
                    cameraTest = testsList[testsListIndex],
                    readyForTest = true
                ),
            )
        } else {
            cameraMutableState.value = cameraState.value.copy(
                screenState = CameraScreenState.FinishTests
            )
        }
    }
}

data class CameraState(
    val screenState: CameraScreenState = CameraScreenState.Initial
)

sealed class CameraAction {
    data class TestResultAction(val isPassed: Boolean) : CameraAction()
    data class PhotoCapturedAction(val uri: Uri) : CameraAction()
}

sealed class CameraScreenState {
    object Initial : CameraScreenState()
    data class Execute(
        val testIndex: Int,
        val testResultValue: TestResultValue = TestResultValue.UNKNOWN,
        val cameraTest: com.example.feature_test_camera.ICameraTest? = null,
        val readyForTest: Boolean = false,
    ) : CameraScreenState()

    data class PhotoCaptured(val uri: Uri) : CameraScreenState()

    object FinishTests : CameraScreenState()
}