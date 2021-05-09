package com.apps.bacon.shoppinglistapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NameValidationTest {
    @Test
    fun `empty name returns false`() {
        val result = NameValidation.validate("")
        assertThat(result).isFalse()
    }

    @Test
    fun `name with no letters returns false`() {
        val result = NameValidation.validate("111")
        assertThat(result).isFalse()
    }

    @Test
    fun `name with at least one letter returns true`() {
        val result = NameValidation.validate("7weeks")
        assertThat(result).isTrue()
    }

    @Test
    fun `name with special characters returns false`() {
        val result = NameValidation.validate("name%")
        assertThat(result).isFalse()
    }

    @Test
    fun `name with space character returns true`() {
        val result = NameValidation.validate("na me")
        assertThat(result).isTrue()
    }
}