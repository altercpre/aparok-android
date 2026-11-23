package io.aparok.app.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.aparok.app.core_ui.features.editor.BlockViewHolder
import io.aparok.app.utils.espresso.WithMarginStart
import io.aparok.app.utils.espresso.WithPaddingLeft

fun <T : BlockViewHolder> Int.scrollTo(position: Int) {
    onView(withId(this)).perform(RecyclerViewActions.scrollToPosition<T>(position))
}

fun ViewInteraction.checkHasMarginStart(dimen: Int, coefficient: Int) {
    check(matches(WithMarginStart(dimen, coefficient)))
}

fun ViewInteraction.checkHasPaddingLeft(dimen: Int, coefficient: Int) {
    check(matches(WithPaddingLeft(dimen, coefficient)))
}