package com.example.composeapp.test_screens.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.composeapp.base.ui.hasNext
import com.example.test_core.data.TestResultValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.immutableListOf

class CameraScreenViewModel : ViewModel() {

    // состояние экрана, сопровождающего тест
    private val cameraMutableState = MutableStateFlow(CameraState())
    val cameraState = cameraMutableState.asStateFlow()

    private val testsList = immutableListOf<ICameraTest>(
        BackCameraTest(),
        FrontCameraTest(),
        AutofocusBarcodeTest()
    )
    private var testsListIndex = 0
    private var testsResults = mutableMapOf<ICameraTest, TestResultValue>()

    init {
        cameraMutableState.value = cameraState.value.copy(
            screenState = CameraScreenState.Execute(
                testIndex = testsListIndex,
                cameraTest = testsList[testsListIndex],
                readyForTest = true
            ),
        )
    }

    fun saveTestResultValue(isPassed: Boolean) {
        testsResults[testsList[testsListIndex]] =
            if (isPassed) TestResultValue.PASSED else TestResultValue.FAILED
        runNextText()
    }

    fun onPhotoCaptured(uri: Uri) {
        cameraMutableState.value = cameraState.value.copy(
            screenState = CameraScreenState.PhotoCaptured(uri)
        )
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
        }
    }
}

data class CameraState(
    val screenState: CameraScreenState = CameraScreenState.Initial
)

sealed class CameraScreenState {
    object Initial : CameraScreenState()
    data class Execute(
        val testIndex: Int,
        val testResultValue: TestResultValue = TestResultValue.UNKNOWN,
        val cameraTest: ICameraTest? = null,
        val readyForTest: Boolean = false,
    ) : CameraScreenState()

    data class PhotoCaptured(val uri: Uri) : CameraScreenState()
}