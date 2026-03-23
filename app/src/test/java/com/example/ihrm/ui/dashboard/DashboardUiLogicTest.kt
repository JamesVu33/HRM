package com.example.ihrm.ui.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardUiLogicTest {

    @Test
    fun securityRejectedShare_matchesTotal() {
        assertEquals(0.75f, securityRejectedShare(approved = 0, rechecking = 1, rejected = 3), 0.001f)
    }

    @Test
    fun securityRejectedShare_zeroTotal_returnsZero() {
        assertEquals(0f, securityRejectedShare(0, 0, 0), 0f)
    }
}
