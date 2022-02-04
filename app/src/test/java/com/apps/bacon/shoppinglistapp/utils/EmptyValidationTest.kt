package com.apps.bacon.shoppinglistapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EmptyValidationTest {
    @Test
    fun `empty value returns false`() {
        val result = EmptyValidation.validate("")
        assertThat(result).isFalse()
    }

    @Test
    fun `value that contains text returns true`() {
        val result = EmptyValidation.validate("some text")
        assertThat(result).isTrue()
    }

    @Test
    fun `value that contains only withe spaces returns false`() {
        val result = EmptyValidation.validate("              ")
        assertThat(result).isFalse()
    }
}