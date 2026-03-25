package com.example.ihrm.ui.calendar.management

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarLeaveFilterLogicTest {

    @Test
    fun emptyApplied_returnsEmptyMap() {
        val out = buildFilteredDotMapByDay(
            mapOf(2 to setOf(LeaveFilterType.ANNUAL)),
            emptySet()
        )
        assertTrue(out.isEmpty())
    }

    @Test
    fun filtersToAnnualOnly() {
        val out = buildFilteredDotMapByDay(
            mapOf(
                2 to setOf(LeaveFilterType.ANNUAL, LeaveFilterType.MATERNITY),
                15 to setOf(LeaveFilterType.SICK)
            ),
            setOf(LeaveFilterType.ANNUAL)
        )
        assertEquals(listOf(0xFF2B7FFFL), out[2])
        assertFalse(out.containsKey(15))
    }

    @Test
    fun multipleTypes_respectsMaxThreeDots() {
        val out = buildFilteredDotMapByDay(
            mapOf(
                1 to setOf(
                    LeaveFilterType.ANNUAL,
                    LeaveFilterType.PERSONAL,
                    LeaveFilterType.SICK,
                    LeaveFilterType.MATERNITY
                )
            ),
            LeaveFilterType.All
        )
        assertEquals(
            listOf(0xFF2B7FFFL, 0xFF00BBA7L, 0xFFFB2C36L),
            out[1]
        )
    }
}
