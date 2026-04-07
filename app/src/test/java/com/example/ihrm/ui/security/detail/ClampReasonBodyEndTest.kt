package com.example.ihrm.ui.security.detail

import org.junit.Assert.assertEquals
import org.junit.Test

class ClampReasonBodyEndTest {

    @Test
    fun mapsLineEndMinusPrefix() {
        assertEquals(5, clampReasonBodyEnd(lineEndExclusiveInFull = 13, reasonPrefixLength = 8, reasonBodyLength = 100))
    }

    @Test
    fun clampsToBodyLength() {
        assertEquals(10, clampReasonBodyEnd(lineEndExclusiveInFull = 100, reasonPrefixLength = 8, reasonBodyLength = 10))
    }

    @Test
    fun clampsNegativeToZero() {
        assertEquals(0, clampReasonBodyEnd(lineEndExclusiveInFull = 3, reasonPrefixLength = 8, reasonBodyLength = 10))
    }
}
