package com.example.ihrm.ui.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardMockDataTest {

    @Test
    fun avatarInitialsFromDisplayName_twoWords() {
        assertEquals("TL", DashboardMockData.avatarInitialsFromDisplayName("Tiểu Long Nữ"))
    }

    @Test
    fun avatarInitialsFromDisplayName_singleWord() {
        assertEquals("AL", DashboardMockData.avatarInitialsFromDisplayName("Alice"))
    }
}
