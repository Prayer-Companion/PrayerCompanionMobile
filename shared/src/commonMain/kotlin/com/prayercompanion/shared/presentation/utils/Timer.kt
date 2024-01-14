package com.prayercompanion.shared.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class Timer(private val onTick: (Int) -> Unit, private val onFinish: () -> Unit) {
    private var timer: Job? = null

    fun start(seconds: Int, counter: CounterType): Timer {
        val onTick = onTick
        val onFinish = onFinish
        timer = CoroutineScope(EmptyCoroutineContext).launch {
            repeat(seconds) {
                val value = if (counter == CounterType.UP) it else (seconds - it)
                onTick(value)
                delay(1000)
            }
            onFinish()
        }
        return this
    }

    fun stop() {
        timer?.cancel()
    }

    enum class CounterType {
        UP, DOWN
    }
}