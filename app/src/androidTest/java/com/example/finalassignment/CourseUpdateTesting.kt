package com.example.finalassignment

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.ui.fragments.mycourse.CourseUpdate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class CourseUpdateTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(CourseUpdate::class.java)

    @Test
    fun CourseUpdated(){

        ServiceBuilder.token = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MDVhMTRlNTM3NTNkNjI1MzRhNTk3MzkiLCJpYXQiOjE2MTg3NTEzNTN9.tcDTaSnUs4cRNeuxcvYFIxD2VNwvBTBmB7FJ8u7eA98"

        Espresso.onView(ViewMatchers.withId(R.id.etupdatecoursetitle))
            .perform(ViewActions.typeText("Kotlin"))

        Espresso.onView(ViewMatchers.withId(R.id.etupdatecoursedesc))
            .perform(ViewActions.typeText("Kotlin Programing"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etupdatefiletitle))
            .perform(ViewActions.typeText("Kotlin"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnupdatecourse))
            .perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.txtandroidbtn))
            //.check(ViewAssertion.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("View Resources")))

    }

}