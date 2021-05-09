package com.apps.bacon.shoppinglistapp.utils

/*
Amount validation:
    if is null or empty
    if contains others numbers than 0
    if contains special characters
 */

object AmountValidation {
    fun validate(charSequence: CharSequence?): Boolean {
        val amount = charSequence?.trim()
        return !(amount.isNullOrEmpty() || !amount.contains(Regex("[1-9]")) || amount.contains(Regex("[^0-9]")))
    }
}