package com.apps.bacon.shoppinglistapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AmountValidationTest {
    @Test
    fun `empty amount returns false`() {
        val result = AmountValidation.validate("")
        assertThat(result).isFalse()
    }

    @Test
    fun `amount that contains only 0 returns false`() {
        val result = AmountValidation.validate("00")
        assertThat(result).isFalse()
    }

    @Test
    fun `amount that contains special characters returns false`() {
        val result = AmountValidation.validate("123$")
        assertThat(result).isFalse()
    }
}