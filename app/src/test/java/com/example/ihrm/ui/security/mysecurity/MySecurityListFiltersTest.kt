package com.example.ihrm.ui.security.mysecurity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MySecurityListFiltersTest {

    @Test
    fun default_hasNoActiveFilters() {
        assertFalse(MySecurityListFilters.Default.hasActiveFilters())
    }

    @Test
    fun hasActiveFilters_whenStatusOrYearSet() {
        assertTrue(MySecurityListFilters(status = MySecurityListStatusFilter.APPROVED).hasActiveFilters())
        assertTrue(MySecurityListFilters(year = 2024).hasActiveFilters())
    }

    @Test
    fun statusApiParam_mapsToBackendValues() {
        assertNull(MySecurityListStatusFilter.ALL.toApiStatusParam())
        assertEquals("submitted", MySecurityListStatusFilter.SUBMITTED.toApiStatusParam())
        assertEquals("rejected", MySecurityListStatusFilter.REJECTED.toApiStatusParam())
        assertEquals("approved", MySecurityListStatusFilter.APPROVED.toApiStatusParam())
    }
}
