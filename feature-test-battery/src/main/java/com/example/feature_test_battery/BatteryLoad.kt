package com.example.feature_test_battery

import android.os.CountDownTimer
import android.os.Looper
import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.TestResultValue
import com.example.test_core.data.TestState
import java.io.File
import java.io.FileFilter
import java.lang.Integer.max
import java.util.Collections
import java.util.concurrent.Executors
import java.util.regex.Pattern
import kotlin.math.abs


class BatteryLoad(
    override var options: List<BaseTestOption>,
) : IBatteryTest() {

    data class BatteryLoadOptions(
        val startBatteryLevel: Int = 0,
        val minStartLevel: Int = 30,
        val testTime: Int = 60,
        val dischargeThreshold: Int = 5,
        val enableVibro: Boolean = true,
        val enable3d: Boolean = true,
        val endBatteryLevel: Int = 0,
    )

    override var timerTicker: TimerTicker? = null
    override var isRunning = false

    override var testOptions: BatteryLoadOptions = BatteryLoadOptions()
    private var timer: CountDownTimer? = null
    private val mThreadList = Collections.synchronizedList(ArrayList<Thread>())

    init {
        options.forEach {
            if (it.name == "testTime") {
                testOptions = testOptions.copy(testTime = it.value!!)
            }
            if (it.name == "dischargeThreshold") {
                testOptions = testOptions.copy(dischargeThreshold = it.value!!)
            }
        }
    }

    override fun setStartBatteryLevel(startBatteryLevel: Int) {
        testOptions = testOptions.copy(startBatteryLevel = startBatteryLevel)
    }

    override fun setEndBatteryLevel(endBatteryLevel: Int) {
        testOptions = testOptions.copy(endBatteryLevel = endBatteryLevel)
    }

    override fun getRealDischargeThreshold(): Int {
        return abs(testOptions.startBatteryLevel - testOptions.endBatteryLevel)
    }

    private fun computeResult() {
        setTestResultValue(
            if (getRealDischargeThreshold() <= testOptions.dischargeThreshold) {
                TestResultValue.PASSED
            } else {
                TestResultValue.FAILED
            }
        )
    }

    override fun execute() {
        isRunning = true
        timer = object : CountDownTimer(testOptions.testTime * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTicker?.onUpdateTimerInSeconds(millisUntilFinished.toInt() / 1000)
            }

            override fun onFinish() {
                stop()
                computeResult()
                testMutableState.value = TestState.Completed
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
        timerTicker = null
    }

    override fun hardStop() {
        stop()
        setTestResultValue(TestResultValue.SKIPPED)
        testMutableState.value = TestState.Completed
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