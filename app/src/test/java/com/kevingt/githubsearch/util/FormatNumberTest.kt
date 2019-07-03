package com.kevingt.githubsearch.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatNumberTest {
    companion object {
        private const val NUM1 = 965
        private const val NUM2 = 1000
        private const val NUM3 = 1001
        private const val NUM4 = 1099
        private const val NUM5 = 2999
        private const val NUM6 = 3499
        private const val NUM7 = 5890
    }

    @Test
    fun formatNumberTest() {
        assertEquals("965", NUM1.formatNumber())
        assertEquals("1k", NUM2.formatNumber())
        assertEquals("1k", NUM3.formatNumber())
        assertEquals("1k", NUM4.formatNumber())
        assertEquals("2.9k", NUM5.formatNumber())
        assertEquals("3.4k", NUM6.formatNumber())
        assertEquals("5.8k", NUM7.formatNumber())
    }
}