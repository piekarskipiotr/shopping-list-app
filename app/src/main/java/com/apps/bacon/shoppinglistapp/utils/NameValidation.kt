package com.apps.bacon.shoppinglistapp.utils

/*
Name validation:
    if is null or empty
    if contains at least one letter
    if contains special characters ('space' is allowed)
 */

object NameValidation {
    fun validate(charSequence: CharSequence?): Boolean {
        val name = charSequence?.trim()
        return !(name.isNullOrEmpty() || !name.contains(Regex("[A-Za-z\\p{L}]")) || name.contains(Regex("[^A-Za-z0-9\\s\\p{L}]")))
    }
}