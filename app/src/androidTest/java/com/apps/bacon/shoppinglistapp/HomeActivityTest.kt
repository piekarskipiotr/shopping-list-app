package com.apps.bacon.shoppinglistapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.apps.bacon.shoppinglistapp.ui.home.HomeActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

@LargeTest
class HomeActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testInsertNewShoppingList() {
        onView(withId(R.id.addShoppingListButton)).perform(click())
        val shoppingListName = "Test list"
        onView(withId(R.id.nameTextInput)).perform(ViewActions.typeText(shoppingListName))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.addNewShoppingListButton)).perform(click())

        onView(withText(shoppingListName)).check(matches(isDisplayed()))
    }

    @Test
    fun testIfAddingButtonIsDisabledOnStart() {
        onView(withId(R.id.addShoppingListButton)).perform(click())
        onView(withId(R.id.addNewShoppingListButton)).check(matches( not(isClickable())))
    }

    @Test
    fun testIfAddingButtonIsDisabledOnEmptyName() {
        onView(withId(R.id.addShoppingListButton)).perform(click())
        val testName = ""
        onView(withId(R.id.nameTextInput)).perform(ViewActions.typeText(testName))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.addNewShoppingListButton)).check(matches( not(isClickable())))
    }

    @Test
    fun testIfAddingButtonIsDisabledOnNameThatContainsOnlyNumbers() {
        onView(withId(R.id.addShoppingListButton)).perform(click())
        val testName = "1234"
        onView(withId(R.id.nameTextInput)).perform(ViewActions.typeText(testName))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.addNewShoppingListButton)).check(matches( not(isClickable())))
    }

    @Test
    fun testIfAddingButtonIsDisabledOnNameThatContainsSpecialCharacters() {
        onView(withId(R.id.addShoppingListButton)).perform(click())
        val testName = "c:"
        onView(withId(R.id.nameTextInput)).perform(ViewActions.typeText(testName))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.addNewShoppingListButton)).check(matches( not(isClickable())))
    }
}