package com.example.beerapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.beerapp.presentation.MainActivity
import com.example.beerapp.presentation.beerlist.BeerPagingAdapter.MyViewHolder
import org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BeerDetailsFragmentTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_isBeerDetailsFragmentVisible_onListItemClick() {
        onView(withId(R.id.beerList)).perform(
            actionOnItemAtPosition<MyViewHolder>(
                2, click()
            )
        )

        onView(withId(R.id.iv_beer)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_beerName)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_abv)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_year)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_descriptionTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_Description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_informationTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.info_txt)).check(matches(isDisplayed()))
    }

}