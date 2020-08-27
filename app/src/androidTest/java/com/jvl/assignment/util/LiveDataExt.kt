package com.jvl.assignment.util

import androidx.lifecycle.LiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Gets live data after a certain delay (default is 2 seconds)
 */
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getValueDelayed(waitTime: Long = 2): T{
    var data: T? = null
    val latch = CountDownLatch(1)
    this.observeForever { o ->
        data = o
        latch.countDown()
    }
    latch.await(waitTime, TimeUnit.SECONDS)

    return data!!
}