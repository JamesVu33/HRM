package com.example.ihrm.ui.dashboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

class DashboardManagementCalendarDateTest {

    @Test
    fun localeForAppLanguageCode_mapsViKoEn() {
        assertEquals(Locale("vi", "VN"), localeForAppLanguageCode("vi"))
        assertEquals(Locale("ko", "KR"), localeForAppLanguageCode("KO"))
        assertEquals(Locale.US, localeForAppLanguageCode("en"))
        assertEquals(Locale.US, localeForAppLanguageCode("unknown"))
    }

    @Test
    fun formatManagementCalendarDate_sameDay_allLocales() {
        val fixed = LocalDate.of(2026, 4, 9)
        val en = formatManagementCalendarDate(fixed, "en")
        val vi = formatManagementCalendarDate(fixed, "vi")
        val ko = formatManagementCalendarDate(fixed, "ko")
        assertEquals("9", en.dayOfMonth)
        assertEquals("9", vi.dayOfMonth)
        assertEquals("9", ko.dayOfMonth)
        assertTrue(en.monthLabel.isNotBlank())
        assertTrue(vi.monthLabel.isNotBlank())
        assertTrue(ko.monthLabel.isNotBlank())
        assertTrue(en.weekdayLabel.isNotBlank())
        assertTrue(vi.weekdayLabel.isNotBlank())
        assertTrue(ko.weekdayLabel.isNotBlank())
    }

    @Test
    fun formatManagementCalendarDate_monthDiffersByLocale() {
        val fixed = LocalDate.of(2026, 1, 15)
        val en = formatManagementCalendarDate(fixed, "en")
        val ko = formatManagementCalendarDate(fixed, "ko")
        assertNotEquals(en.monthLabel, ko.monthLabel)
    }

    @Test
    fun formatDashboardHomeDateLine_fullStyle_nonBlankPerLanguage() {
        val fixed = LocalDate.of(2026, 4, 9)
        listOf("en", "vi", "ko").forEach { code ->
            val line = formatDashboardHomeDateLine(fixed, code)
            assertTrue(line.isNotBlank())
        }
    }
}
