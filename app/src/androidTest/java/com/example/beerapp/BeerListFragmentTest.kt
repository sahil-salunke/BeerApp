package com.example.beerapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.beerapp.common.FakeBeerData
import com.example.beerapp.common.waitUntilViewIsDisplayed
import com.example.beerapp.presentation.MainActivity
import com.example.beerapp.presentation.beerlist.BeerPagingAdapter.MyViewHolder
import org.hamcrest.Matchers.allOf
import org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BeerListFragmentTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val beerInTest = FakeBeerData.beersList

    @Test
    fun test_isBeerListFragmentVisible_onAppLaunch() {
        waitUntilViewIsDisplayed(withId(R.id.beerList))
        onView(withId(R.id.beerList)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isBeerListViewsVisible_onAppLaunch() {
        waitUntilViewIsDisplayed(withId(R.id.beerList))
        onView(allOf(withId(R.id.beerList), isDisplayed())).perform(swipeUp())
    }

    @Test
    fun test_performListItemClick() {
        onView(withId(R.id.beerList)).perform(scrollToPosition<MyViewHolder>(15))
        onView(withId(R.id.beerList)).perform(actionOnItemAtPosition<MyViewHolder>(12, click()))
        onView(withId(R.id.tv_beerName)).check(matches(isDisplayed()))
    }

    @Test
    fun test_backNavigation_toBeerListFragment() {
        onView(withId(R.id.beerList)).perform(scrollToPosition<MyViewHolder>(15));
        onView(withId(R.id.beerList)).perform(actionOnItemAtPosition<MyViewHolder>(12, click()));
        onView(withId(R.id.tv_beerName)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.beerList)).check(matches(isDisplayed()))
    }
}