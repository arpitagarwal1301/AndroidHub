package com.agarwal.arpit.androidhub.flashit

class Time {

    private var freq: Int = 0

    val sleepTime: Long
        get() = Math.floor(((11 - freq) * 100).toDouble()).toLong()

    fun setSleepTime(freq: Int) {
        this.freq = freq
    }

}