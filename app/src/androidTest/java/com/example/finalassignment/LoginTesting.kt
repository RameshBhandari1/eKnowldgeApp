package com.example.finalassignment

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.finalassignment.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLogin(){
        onView(withId(R.id.etuser))
            .perform(typeText("kushal"))

        onView(withId(R.id.etpass))
            .perform(typeText("kushal123"))

        closeSoftKeyboard()

        onView(withId(R.id.btnlogin))
            .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.txtandroidbtn))
            //.check(ViewAssertion.matches(ViewMatchers.isDisplayed()))
            .check(matches(withText("View Resources")))

    }
}