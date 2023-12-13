package com.example.composeapp.tests.audio

class SilenceDetector(
    sampleRate: Int, time_sec: Float,
    private val mThreshold: Int, private val mPeakCountThreshold: Int
) {
    private val mSilentSamplesThreshold: Int
    private var mSilentSamples = 0

    init {
        mSilentSamplesThreshold = (sampleRate * time_sec).toInt()
    }

    fun analyze(buff: ShortArray, size: Int): Boolean {
        var peaks = 0
        var s = 0
        while (size != s && peaks < mPeakCountThreshold) {
            if (Math.abs(buff[s].toInt()) > mThreshold) ++peaks
            ++s
        }
        if (peaks >= mPeakCountThreshold) {
            mSilentSamples = 0
            return false
        }
        mSilentSamples += size
        return mSilentSamples > mSilentSamplesThreshold
    }
}