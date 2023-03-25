package com.example.beerapp

import org.mockito.Mockito

open class MockitoUtils {
    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
}