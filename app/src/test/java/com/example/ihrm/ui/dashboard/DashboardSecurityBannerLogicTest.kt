package com.example.ihrm.ui.dashboard

import com.example.ihrm.R
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardSecurityBannerLogicTest {

    @Test
    fun aggregate_countsStatuses() {
        val list = listOf(
            MySecurityCheckResponse(status = "approved"),
            MySecurityCheckResponse(status = "rejected"),
            MySecurityCheckResponse(status = "submitted"),
            MySecurityCheckResponse(status = "pending"),
            MySecurityCheckResponse(status = "unknown")
        )
        val c = aggregateSecuritySubmissions(list)
        assertEquals(5, c.total)
        assertEquals(1, c.approved)
        assertEquals(1, c.rejected)
        assertEquals(1, c.submitted)
        assertEquals(1, c.pending)
        assertEquals(2, c.rechecking)
    }

    @Test
    fun banner_errorWins() {
        val c = SecuritySubmissionCounts(10, 5, 1, 2, 0)
        val b = resolveDashboardSecurityBanner(c, isLoading = false, errorMessage = "boom")
        assertEquals(R.string.dashboard_security_banner_error_title, b.titleRes)
        assertEquals("boom", b.subtitleOverride)
    }

    @Test
    fun banner_submittedInReview() {
        val c = SecuritySubmissionCounts(3, 0, 0, 1, 0)
        val b = resolveDashboardSecurityBanner(c, isLoading = false, errorMessage = null)
        assertEquals(R.string.dashboard_security_banner_review_title, b.titleRes)
        assertEquals(R.string.dashboard_security_banner_review_subtitle, b.subtitleRes)
        assertEquals(null, b.subtitleOverride)
    }

    @Test
    fun banner_actionRequired() {
        val c = SecuritySubmissionCounts(2, 0, 2, 0, 0)
        val b = resolveDashboardSecurityBanner(c, isLoading = false, errorMessage = null)
        assertEquals(R.string.dashboard_security_banner_action_title, b.titleRes)
        assertEquals(R.string.dashboard_security_banner_rejected_subtitle, b.subtitleRes)
    }

    @Test
    fun banner_emptyWhenNotLoading() {
        val c = SecuritySubmissionCounts(0, 0, 0, 0, 0)
        val b = resolveDashboardSecurityBanner(c, isLoading = false, errorMessage = null)
        assertEquals(R.string.dashboard_security_banner_empty_title, b.titleRes)
        assertEquals(R.string.dashboard_security_banner_empty_subtitle, b.subtitleRes)
    }

    @Test
    fun banner_whileLoading_emptyList_staysAllCaughtUp() {
        val c = SecuritySubmissionCounts(0, 0, 0, 0, 0)
        val b = resolveDashboardSecurityBanner(c, isLoading = true, errorMessage = null)
        assertEquals(R.string.dashboard_security_banner_all_good_title, b.titleRes)
    }
}
