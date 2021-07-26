package com.example.finalassignment

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.ui.fragments.mycourse.CourseUpdate
import com.example.finalassignment.ui.fragments.profile.ProfileUpdateActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class UpdateProfileTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(ProfileUpdateActivity::class.java)

    @Test
    fun ProfileUpdated(){

        ServiceBuilder.token = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MDVhMTRlNTM3NTNkNjI1MzRhNTk3MzkiLCJpYXQiOjE2MTg3NTA4Mjd9.OI9DHWO25seMOsktxahT4BBY-DOLMKNEUEcTkBFP0yY"

        Thread.sleep(3000)

        Espresso.onView(ViewMatchers.withId(R.id.etPFullname))
            .perform(ViewActions.typeText("Kushal Pandey"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etPBio))
            .perform(ViewActions.typeText("I have 7 years experiences in Programming."))

        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.etPUsername))
            .perform(ViewActions.typeText("kushal"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etPPhone))
            .perform(ViewActions.typeText("9863622122"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etPEmail))
            .perform(ViewActions.typeText("kushal@gmail.com"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etPAddress))
            .perform(ViewActions.typeText("Damak"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnPeditprofile))
            .perform(ViewActions.click())

        Thread.sleep(3000)

        Espresso.onView(ViewMatchers.withId(R.id.txtandroidbtn))
            //.check(ViewAssertion.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("View Resources")))

    }
}