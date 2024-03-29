package com.example.composeapp.test_screens.battery

import android.app.Activity
import android.content.Context
import android.os.Vibrator
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.composeapp.R
import com.example.composeapp.base.AttachLifecycleEvent
import com.example.composeapp.base.UITestOption
import com.example.composeapp.base.theme.PiterrusAppTheme
import com.example.composeapp.base.theme.backgroundErrorColor
import com.example.composeapp.base.theme.dividerColor
import com.example.composeapp.base.theme.failedColor
import com.example.composeapp.base.theme.gray7C40016TextStyle
import com.example.composeapp.base.theme.grayA240016TextStyle
import com.example.composeapp.base.theme.passedColor
import com.example.composeapp.base.toMmSs
import com.example.composeapp.base.ui_components.StandardButton
import com.example.feature_test_battery.battery_moon.android.GLSurface
import com.example.test_core.data.OptionMeasurementType
import com.example.test_core.data.TestOptionType
import com.example.test_core.data.TestResultValue
import kotlinx.collections.immutable.toImmutableList


private fun Context.setVibration(vibrate: Boolean) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrate) {
        vibrator.vibrate(longArrayOf(0, 1000), 0)
    } else {
        vibrator.cancel()
    }
}

private fun Context.setMaxBrightness(maxBrightness: Boolean) {
    val activity = this as? Activity ?: return
    val layoutParams: WindowManager.LayoutParams = activity.window.attributes
    layoutParams.screenBrightness =
        if (maxBrightness) 1f else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    activity.window.attributes = layoutParams
}

@Composable
fun BatteryView(
    batteryState: BatteryState,
    onStartButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
    onFinishedButtonClicked: () -> Unit
) {
    when (batteryState.screenState) {
        is BatteryScreenState.NotExecuting -> {
            TestNotExecute(
                onStartButtonClicked = onStartButtonClicked,
                onSkipButtonClicked = onFinishedButtonClicked,
                batteryState = batteryState
            )
        }

        is BatteryScreenState.Executing -> {
            TestExecute(
                screenState = batteryState.screenState,
                onSkipButtonClicked = onSkipButtonClicked
            )
        }
    }
}

@Composable
private fun TestNotExecute(
    batteryState: BatteryState,
    onStartButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn {
                    items(batteryState.options) {
                        Divider(
                            color = dividerColor,
                            thickness = 1.dp
                        )
                        SimpleRowTest(
                            testOption = it
                        )
                    }
                    item {
                        Divider(
                            color = dividerColor,
                            thickness = 1.dp
                        )
                    }
                }
                var realDischargeLevelText = ""
                var isPassed = false
                when (batteryState.testResultValue) {
                    com.example.test_core.data.TestResultValue.UNKNOWN -> {}
                    com.example.test_core.data.TestResultValue.PASSED -> {
                        realDischargeLevelText = stringResource(
                            id = R.string.real_discharge_level,
                            batteryState.dischargeThreshold
                        )
                        isPassed = true
                    }

                    com.example.test_core.data.TestResultValue.FAILED -> {
                        realDischargeLevelText = stringResource(
                            id = R.string.real_discharge_level,
                            batteryState.dischargeThreshold
                        )
                        isPassed = false
                    }

                    com.example.test_core.data.TestResultValue.SKIPPED -> {
                        realDischargeLevelText = "Skipped"
                        isPassed = false
                    }
                }
                if (batteryState.testResultValue != com.example.test_core.data.TestResultValue.UNKNOWN) {
                    Spacer(modifier = Modifier.height(15.dp))
                    RealDischargeLevel(
                        text = realDischargeLevelText,
                        isPassed = isPassed
                    )
                }

                if (batteryState.chargeState == ChargeState.Charging) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(backgroundErrorColor)
                    ) {
                        Text(
                            modifier = Modifier.padding(all = 7.dp),
                            text = stringResource(id = R.string.unplug_charger_battery_attention),
                            style = gray7C40016TextStyle.copy(color = Color.White)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                StandardButton(
                    text = stringResource(
                        id = if (batteryState.testResultValue == TestResultValue.UNKNOWN) {
                            R.string.start
                        } else {
                            R.string.retry
                        }
                    ),
                    painted = true,
                    modifier = Modifier.padding(horizontal = 40.dp),
                    enabled = batteryState.readyForTest
                ) {
                    onStartButtonClicked.invoke()
                }
                Spacer(modifier = Modifier.height(12.dp))
                StandardButton(
                    text = stringResource(
                        id = if (batteryState.testResultValue == TestResultValue.UNKNOWN) {
                            R.string.skip
                        } else {
                            R.string.proceed
                        }
                    ),
                    painted = true,
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    onSkipButtonClicked.invoke()
                }
            }
        }
    }
}

@Composable
private fun TestExecute(
    screenState: BatteryScreenState.Executing,
    onSkipButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    AttachLifecycleEvent(
        onResumeCallback = {
            context.setVibration(screenState.enabledVibro)
            context.setMaxBrightness(screenState.enabled3D)
        },
        onDisposeCallback = {
            context.setVibration(false)
            context.setMaxBrightness(false)
        }
    )
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        if (screenState.enabled3D) {
            AndroidView(
                factory = {
                    GLSurface(it)
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = screenState.timeToCompletionInSeconds.toMmSs(),
                style = gray7C40016TextStyle.copy(color = Color.Black)
            )
            Text(
                text = "Remaining time",
                style = gray7C40016TextStyle.copy(color = Color.Black)
            )
            StandardButton(
                modifier = Modifier.padding(horizontal = 30.dp),
                text = stringResource(id = R.string.skip),
                painted = true
            ) {
                onSkipButtonClicked.invoke()
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RealDischargeLevel(
    text: String,
    isPassed: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(
                id = if (isPassed) R.drawable.ic_check_passed else R.drawable.ic_check_failed
            ),
            contentDescription = stringResource(id = if (isPassed) R.string.passed else R.string.failed)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            style = gray7C40016TextStyle.copy(color = if (isPassed) passedColor else failedColor)
        )
    }
}

@Composable
fun SimpleRowTest(
    testOption: UITestOption
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = testOption.optionDisplayName,
            modifier = Modifier.weight(1F),
            style = gray7C40016TextStyle
        )
        Text(
            text = when (testOption.optionMeasurement) {
                OptionMeasurementType.PERCENT -> "${testOption.optionDisplayValue}%"
                OptionMeasurementType.TIME -> testOption.optionDisplayValue.toMmSs()
                else -> ""
            },
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.End,
            style = if (testOption.available == null) {
                grayA240016TextStyle
            } else {
                grayA240016TextStyle.copy(
                    color = if (testOption.available) passedColor else failedColor
                )
            }
        )
    }
}

@Preview(showSystemUi = true, device = "id:pixel_6_pro")
@Composable
fun BatteryView_Preview() {
    val options = listOf(
        UITestOption(
            optionDisplayName = "Battery level",
            optionDisplayValue = 50,
            optionMeasurement = OptionMeasurementType.PERCENT,
            available = false,
            showedInList = true,
            testOptionType = TestOptionType.UNDEFINED
        ),
        UITestOption(
            optionDisplayName = "Discharge threshold",
            optionDisplayValue = 50,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true,
            testOptionType = TestOptionType.UNDEFINED
        ),
        UITestOption(
            optionDisplayName = "Test time",
            optionDisplayValue = 5,
            optionMeasurement = OptionMeasurementType.TIME,
            showedInList = true,
            testOptionType = TestOptionType.UNDEFINED
        ),
        UITestOption(
            optionDisplayName = "Charging",
            optionDisplayValue = 50,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true,
            testOptionType = TestOptionType.UNDEFINED
        ),
        UITestOption(
            optionDisplayName = "Health",
            optionDisplayValue = 50,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true,
            testOptionType = TestOptionType.UNDEFINED
        ),
        UITestOption(
            optionDisplayName = "Charge cycles",
            optionDisplayValue = 50,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = false,
            testOptionType = TestOptionType.UNDEFINED
        ),
    )
    PiterrusAppTheme {
        BatteryView(
            batteryState = BatteryState(
                chargeState = ChargeState.Charging,
                screenState = BatteryScreenState.NotExecuting,
                options = options.toImmutableList(),
            ),
            onSkipButtonClicked = {},
            onStartButtonClicked = {},
            onFinishedButtonClicked = {}
        )
    }
}