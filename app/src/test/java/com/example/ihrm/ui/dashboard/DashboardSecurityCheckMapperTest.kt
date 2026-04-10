package com.example.ihrm.ui.dashboard

import com.example.ihrm.data.remote.securities.SecurityCheckDashboardResponse
import com.example.ihrm.data.remote.securities.StatusCountDto
import com.example.ihrm.data.remote.securities.SubmissionSummaryDto
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardSecurityCheckMapperTest {

    @Test
    fun managementPendingCount_matchesIosFormula() {
        assertEquals(0, managementPendingCount(0, 5))
        assertEquals(5, managementPendingCount(10, 5))
        assertEquals(0, managementPendingCount(3, 10))
    }

    @Test
    fun mapStatsThisMonthToMonthly_groupsStatuses() {
        val stats = listOf(
            StatusCountDto("APPROVED", 4),
            StatusCountDto("SUBMITTED", 2),
            StatusCountDto("REJECTED", 1),
        )
        val m = mapStatsThisMonthToMonthly(stats)
        assertEquals(4, m.approved)
        assertEquals(2, m.rechecking)
        assertEquals(1, m.rejected)
    }

    @Test
    fun mapSecurityCheckDashboardToManagementSecurity_usesSubmittedMinusApproved() {
        val response = SecurityCheckDashboardResponse(
            submissionSummary = SubmissionSummaryDto(
                totalSubmissions = 100,
                submittedCount = 10,
                notSubmittedCount = 5,
                totalUsers = 50,
            ),
            statsThisMonth = listOf(
                StatusCountDto("APPROVED", 7),
                StatusCountDto("SUBMITTED", 3),
            )
        )
        val m = mapSecurityCheckDashboardToManagementSecurity(response)
        assertEquals(50, m.totalUsers)
        assertEquals(100, m.submissionsCount)
        assertEquals(10, m.submitted)
        assertEquals(3, m.pending) // max(0, 10 - 7)
        assertEquals(5, m.notSubmitted)
    }
}
