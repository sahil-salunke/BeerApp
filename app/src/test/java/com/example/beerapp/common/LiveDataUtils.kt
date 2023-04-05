package com.example.beerapp.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// An extension function for Livedata
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1) // Class who blocks thread till the task gets completed
    val observer = object : Observer<T> { // Observer is applied
        override fun onChanged(o: T?) { // Once data change this method will get called
            data = o
            latch.countDown() // Once data received let the observer know
            this@getOrAwaitValue.removeObserver(this) // remove observer
        }
    }
    this.observeForever(observer) // observer set to livedata

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    // Only wait for 2 sec else exit
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
suspend fun <T> LiveData<T>.observeForTesting(block: suspend () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}