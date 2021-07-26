package com.example.finalassignment

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.finalassignment.ui.LoginActivity
import com.example.finalassignment.ui.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class RegistrationTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun UserRegister(){
        Espresso.onView(ViewMatchers.withId(R.id.rblearner))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.etfullname))
            .perform(ViewActions.typeText("Rakesh Thapa"))

        Espresso.onView(ViewMatchers.withId(R.id.etemail))
            .perform(ViewActions.typeText("rakesh@gmail.com"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etusername))
            .perform(ViewActions.typeText("rakesh"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etpassword))
            .perform(ViewActions.typeText("rakesh123"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etcpassword))
            .perform(ViewActions.typeText("rakesh123"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnregister))
            .perform(ViewActions.click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.tvsignup))
            //.check(ViewAssertion.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("Sign Up Now!")))

    }
}