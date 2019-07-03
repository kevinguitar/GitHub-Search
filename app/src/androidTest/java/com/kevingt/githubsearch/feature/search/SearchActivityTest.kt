package com.kevingt.githubsearch.feature.search


import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.base.BaseUITest
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest : BaseUITest() {

    companion object {
        private const val KEYWORD = "android"
        private const val NO_RESULT_KEYWORD = "aweiflnaefknvazdndfijvspodi"
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun searchActivityTest() {
        // Assert the right description in startup screen
        onView(withId(R.id.tv_search_description_message))
            .check(matches(withText(R.string.search_content_description_welcome)))
            .check(matches(isDisplayed()))

        // Set keyword in EditText and search
        onView(withId(R.id.et_search_keywords))
            .perform(replaceText(KEYWORD))

        onView(withId(R.id.iv_search_icon))
            .perform(click())

        // Check state after network call finished
        onView(withId(R.id.cg_search_sort))
            .check(matches(isDisplayed()))

        // Checked the chip
        onView(withId(R.id.cp_search_stars))
            .perform(click())
            .check(matches(isChecked()))

        // Rotate the screen
        mActivityTestRule.activity.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Checked if the chip still checked
        onView(withId(R.id.cp_search_stars))
            .check(matches(isChecked()))

        // Checked the EditText content
        onView(withId(R.id.et_search_keywords))
            .check(matches(withText(KEYWORD)))

        // Rotate back
        mActivityTestRule.activity.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Clear the keyword and perform search
        onView(withId(R.id.et_search_keywords))
            .perform(replaceText(""))

        onView(withId(R.id.iv_search_icon))
            .perform(click())

        // Check if the empty keyword hint will show in toast
        onView(withText(R.string.search_empty_keywords))
            .inRoot(withDecorView(not(mActivityTestRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        // Set a keyword that will lead to no results and search
        onView(withId(R.id.et_search_keywords))
            .perform(replaceText(NO_RESULT_KEYWORD))

        onView(withId(R.id.iv_search_icon))
            .perform(click())

        // Check if the no result hint will show
        onView(withId(R.id.tv_search_description_message))
            .check(matches(withText(R.string.search_content_description_no_results)))
            .check(matches(isDisplayed()))
    }
}
