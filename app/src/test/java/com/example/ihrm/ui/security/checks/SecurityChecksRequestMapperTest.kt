package com.example.ihrm.ui.security.checks

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SecurityChecksRequestMapperTest {

    @Test
    fun defaultCurrentMonth_fromFirstOfMonthToToday() {
        val f = SecurityChecksActiveFilters.defaultCurrentMonth()
        assertNull(f.groupId)
        assertEquals(SecurityChecksSearchByMode.ALL, f.searchBy)
        val zone = ZoneId.systemDefault()
        val from = Instant.ofEpochMilli(f.dateFromMillis!!).atZone(zone).toLocalDate()
        val to = Instant.ofEpochMilli(f.dateToMillis!!).atZone(zone).toLocalDate()
        val today = LocalDate.now(zone)
        assertEquals(1, from.dayOfMonth)
        assertEquals(today.month, from.month)
        assertEquals(today.year, from.year)
        assertEquals(today, to)
    }

    @Test
    fun toApiQuery_formatsIsoDatesTypeAndGroup() {
        val zone = ZoneId.systemDefault()
        val date = LocalDate.of(2025, 3, 10)
        val start = date.withDayOfMonth(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = date.withDayOfMonth(31).atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()
        val filters = SecurityChecksActiveFilters(
            dateFromMillis = start,
            dateToMillis = end,
            searchBy = SecurityChecksSearchByMode.EMPLOYEE_ID,
            groupId = "grp-1",
        )
        val q = filters.toApiQuery("  xyz  ")
        val fmt = DateTimeFormatter.ISO_INSTANT
        assertEquals(fmt.format(Instant.ofEpochMilli(start)), q.fromDate)
        assertEquals(fmt.format(Instant.ofEpochMilli(end)), q.toDate)
        assertEquals("xyz", q.query)
        assertEquals("EMPLOYEE_ID", q.type)
        assertEquals("grp-1", q.groupId)
        assertNull(q.monthCode)
    }

    @Test
    fun toApiQuery_blankSearch_becomesNullQuery() {
        val filters = SecurityChecksActiveFilters(
            dateFromMillis = null,
            dateToMillis = null,
            searchBy = SecurityChecksSearchByMode.NAME,
            groupId = null,
        )
        val q = filters.toApiQuery("   ")
        assertNull(q.query)
        assertEquals("NAME", q.type)
    }

    @Test
    fun toApiQuery_searchByAll_typeNull() {
        val filters = SecurityChecksActiveFilters(
            dateFromMillis = null,
            dateToMillis = null,
            searchBy = SecurityChecksSearchByMode.ALL,
            groupId = null,
        )
        assertNull(filters.toApiQuery("x").type)
    }
}
