package com.example.ihrm.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DatePickerFormatExtTest {

    @Test
    fun parseDisplayDateToPickerMillis_roundTrip() {
        val ms = "15/04/2026".parseDisplayDateToPickerMillis()!!
        assertEquals("15/04/2026", ms.toPickerDisplayDateString())
    }
}
