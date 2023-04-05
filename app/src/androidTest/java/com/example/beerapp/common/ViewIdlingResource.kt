package com.example.beerapp.common

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewFinder
import org.hamcrest.Matcher
import java.lang.Exception
import java.lang.reflect.Field

class ViewIdlingResource(
    private val viewMatcher: Matcher<View?>,
    private val idleMatcher: Matcher<View?>
) :
    IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "$this $viewMatcher"
    }

    override fun isIdleNow(): Boolean {
        val view: View? = getView(viewMatcher)
        val isIdle: Boolean = idleMatcher.matches(view)
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }


    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    private fun getView(viewMatcher: Matcher<View?>): View? {
        return try {
            val viewInteraction = onView(viewMatcher)
            val finderField: Field? = viewInteraction.javaClass.getDeclaredField("viewFinder")
            finderField?.isAccessible = true
            val finder = finderField?.get(viewInteraction) as ViewFinder
            finder.view
        } catch (e: Exception) {
            null
        }
    }

}