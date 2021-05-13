package com.apps.bacon.shoppinglistapp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.apps.bacon.shoppinglistapp.ui.grocery.GroceryActivity
import org.hamcrest.Matchers.not
import org.junit.Test

@LargeTest
class GroceryActivityTest {
    lateinit var scenario: ActivityScenario<GroceryActivity>

    @Test
    fun testIfButtonIsClickableWhenShoppingListIsArchived() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), GroceryActivity::class.java)
            .putExtra(SHOPPING_LIST_ID_KEY, 1)
            .putExtra(IS_SHOPPING_LIST_ARCHIVED_KEY, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.addGroceryButton)).check(matches(not(isClickable())))
    }

    @Test
    fun testIfButtonIsClickableWhenShoppingListIsUnarchived() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), GroceryActivity::class.java)
            .putExtra(SHOPPING_LIST_ID_KEY, 1)
            .putExtra(IS_SHOPPING_LIST_ARCHIVED_KEY, false)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.addGroceryButton)).check(matches(isClickable()))
    }

    @Test
    fun testInsertNewGrocery() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), GroceryActivity::class.java)
            .putExtra(SHOPPING_LIST_ID_KEY, 1)
            .putExtra(IS_SHOPPING_LIST_ARCHIVED_KEY, false)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.addGroceryButton)).perform(ViewActions.click())
        val groceryName = "Some grocery"
        onView(withId(R.id.groceryNameTextInput)).perform(ViewActions.typeText(groceryName))
        Espresso.closeSoftKeyboard()
        val amount = "8"
        onView(withId(R.id.amountTextInput)).perform(ViewActions.typeText(amount))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.addNewGroceryButton)).perform(ViewActions.click())

        onView(ViewMatchers.withText(groceryName)).check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("X$amount")).check(matches(ViewMatchers.isDisplayed()))
    }

    companion object {
        const val SHOPPING_LIST_ID_KEY = "SHOPPING_LIST_ID"
        const val IS_SHOPPING_LIST_ARCHIVED_KEY = "IS_SHOPPING_LIST_ARCHIVED"
    }
}