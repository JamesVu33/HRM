package com.example.ihrm.ui.calendar.management

import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarMonthGridTest {

    @Test
    fun march2026_starts_sunday_offset_zero() {
        assertEquals(0, firstDaySundayColumnIndex(2026, 3))
    }

    @Test
    fun january2026_starts_thursday_offset_four() {
        assertEquals(4, firstDaySundayColumnIndex(2026, 1))
    }

    @Test
    fun daysInMonth_march_has_31() {
        assertEquals(31, daysInMonth(2026, 3))
    }

    @Test
    fun buildGrid_march2026_has_31_day_slots_and_selected() {
        val grid = buildCalendarGrid42(
            year = 2026,
            month = 3,
            selectedDayOfMonth = 11,
            dotArgbByDay = mapOf(2 to listOf(0xFF2B7FFFL))
        )
        val daySlots = grid.filterIsInstance<CalendarGridSlot.Day>()
        assertEquals(31, daySlots.size)
        val eleven = daySlots.first { it.dayOfMonth == 11 }
        assertEquals(true, eleven.selected)
        assertEquals(false, eleven.mutedBackground)
    }
}
