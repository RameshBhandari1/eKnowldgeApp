package com.example.finalassignment


import android.view.Gravity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.OpenLinkAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.ui.Home_Menu_Activity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class ViewCourseTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(Home_Menu_Activity::class.java)


    @Test
    fun addCourse(){
//        val testRule = ActivityScenarioRule(Home_Menu_Activity::class.java)
        ServiceBuilder.token = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MDVhMTRlNTM3NTNkNjI1MzRhNTk3MzkiLCJpYXQiOjE2MTg2ODA5OTd9.I1rbVsqmllNTLe4JNE6191qtLk-VhaV1NCOCEikKInc"
        Espresso.onView(withId(R.id.txtandroidbtn))
            .perform(click()) // Open Drawer

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.backbuttoncourse))
            .check(matches(ViewMatchers.isDisplayed()))
            //.check(ViewAssertions.matches(ViewMatchers.withText("View Resources")))

    }
}