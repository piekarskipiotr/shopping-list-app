package com.apps.bacon.shoppinglistapp.utils


/*
Empty validation:
    if is null or empty
 */

object EmptyValidation {
    fun validate(charSequence: CharSequence?): Boolean {
        val text = charSequence?.trim()

        return !text.isNullOrEmpty()
    }

    fun validate(string: String?): Boolean {
        val text = string?.trim()

        return !text.isNullOrEmpty()
    }
}