package com.example.composeapp.tests.battery

import android.os.CountDownTimer
import android.os.Looper
import com.example.composeapp.base.data.TestOption
import com.example.composeapp.base.data.TestResultValue
import com.example.composeapp.base.model.BaseTest
import java.io.File
import java.io.FileFilter
import java.lang.Integer.max
import java.util.Collections
import java.util.concurrent.Executors
import java.util.regex.Pattern
import kotlin.math.abs


class BatteryLoad(
    options: List<TestOption>,
    private val onUpdateTimerInSeconds: (Int, enabledVibro: Boolean, enable3d: Boolean) -> Unit,
    private val onCompleted: (TestResultValue, realDischarge: Int?) -> Unit,
) : BaseTest {

    data class BatteryLoadOptions(
        val startBatteryLevel: Int = 0,
        val minStartLevel: Int = 30,
        val testTime: Int = 60,
        val dischargeThreshold: Int = 5,
        val enableVibro: Boolean = true,
        val enable3d: Boolean = true,
        val endBatteryLevel: Int = 0,
    )

    private var testOptions: BatteryLoadOptions = BatteryLoadOptions()
    private var timer: CountDownTimer? = null
    private val mThreadList = Collections.synchronizedList(ArrayList<Thread>())

    override var isRunning = false

    init {
        options.forEach {
            if (it.optionName == "testTime") {
                testOptions = testOptions.copy(testTime = it.optionValue)
            }
            if (it.optionName == "dischargeThreshold") {
                testOptions = testOptions.copy(dischargeThreshold = it.optionValue)
            }
        }
    }

    fun setStartBatteryLevel(startBatteryLevel: Int) {
        testOptions = testOptions.copy(startBatteryLevel = startBatteryLevel)
    }

    fun setEndBatteryLevel(endBatteryLevel: Int) {
        testOptions = testOptions.copy(endBatteryLevel = endBatteryLevel)
    }

    private fun computeResult(): TestResultValue {
        return if (abs(testOptions.startBatteryLevel - testOptions.endBatteryLevel) <= testOptions.dischargeThreshold) TestResultValue.PASSED else TestResultValue.FAILED
    }

    override fun execute() {
        isRunning = true
        timer = object : CountDownTimer(testOptions.testTime * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onUpdateTimerInSeconds.invoke(
                    millisUntilFinished.toInt() / 1000,
                    testOptions.enableVibro,
                    testOptions.enable3d
                )
            }

            override fun onFinish() {
                stop()
                onCompleted.invoke(
                    computeResult(),
                    abs(testOptions.startBatteryLevel - testOptions.endBatteryLevel)
                )
            }

        }
        timer?.start()
        executeUnitOverload()
    }

    override fun stop() {
        isRunning = false
        stopThreads()
        timer?.cancel()
        timer = null

    }

    override fun hardStop() {
        stop()
        onCompleted.invoke(TestResultValue.SKIPPED, null)
    }

    private fun executeUnitOverload() {
        val cores: Int = getCoresCount()
        runThreads(cores)
    }

    private inner class TestRunner : Thread(), Runnable {
        override fun run() {
            Looper.prepare()
            Looper.loop()
            while (isRunning) {
                val d = StrictMath.tan(
                    StrictMath.atan(
                        StrictMath.tan(
                            StrictMath.atan(
                                StrictMath.tan(
                                    StrictMath.atan(
                                        StrictMath.tan(
                                            StrictMath.atan(
                                                StrictMath.tan(
                                                    StrictMath.atan(1.2345678912345679E8)
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
                Math.cbrt(d)
            }
        }
    }

    private fun stopThreads() {
        for (t in mThreadList) t.interrupt()
        mThreadList.clear()
    }

    private fun runThreads(threads: Int) {
        val executor = Executors.newCachedThreadPool()
        for (i in 0..threads) executor.execute(TestRunner())

        var i = 0
        while (threads != i) {
            val t: Thread = TestRunner()
            t.priority = Thread.NORM_PRIORITY
            t.name = "BatteryLoad-$i"
            mThreadList.add(t)
            ++i
        }
        for (t in mThreadList) t.start()
    }

    private val sFilter = FileFilter { pathname: File ->
        Pattern.matches(
            "cpu[0-9]+",
            pathname.name
        )
    }


    private fun getCoresCount(): Int {
        return try {
            val dir = File("/sys/devices/system/cpu/")
            val files: Array<out File>? = dir.listFiles(sFilter)
            files?.size ?: 0
        } catch (e: Exception) {
            max(1, Runtime.getRuntime().availableProcessors())
        }
    }

}