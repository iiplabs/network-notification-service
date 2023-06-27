package com.iiplabs.nns.ksms.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StringUtilTest {

    @Test
    fun testGoodLastField() {
        val source = "card.expiry"
        assertEquals("expiry", StringUtil.getLastField(source))
    }

    @Test
    fun testBadLastField() {
        val source = "expiry"
        assertEquals("expiry", StringUtil.getLastField(source))
    }

    @Test
    fun testMatchAllBetweenBrackets() {
        val fieldsRegex = "^\\((.*?)\\)$".toRegex()
        assertFalse(fieldsRegex matches "")
        assertFalse(fieldsRegex matches "test(test123)1")
        assertTrue(fieldsRegex matches "()")
        assertTrue(fieldsRegex matches "(123)")
        assertTrue(fieldsRegex matches "(test123test)")
    }
}