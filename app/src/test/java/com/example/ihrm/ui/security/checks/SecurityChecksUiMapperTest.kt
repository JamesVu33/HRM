package com.example.ihrm.ui.security.checks

import com.example.ihrm.domain.model.SecurityCheckSubmission
import com.example.ihrm.domain.model.SecurityCheckSubmissionUser
import org.junit.Assert.assertEquals
import org.junit.Test

class SecurityChecksUiMapperTest {

    @Test
    fun filteredBySearchQuery_empty_returnsAll() {
        val list = listOf(sampleSubmission("A"), sampleSubmission("B"))
        assertEquals(2, list.filteredBySearchQuery("", SecurityChecksSearchByMode.ALL).size)
    }

    @Test
    fun filteredBySearchQuery_matchesFullName() {
        val list = listOf(
            sampleSubmission("Alpha", fullName = "Nguyen Van A"),
            sampleSubmission("Beta", fullName = "Tran Thi B"),
        )
        assertEquals(1, list.filteredBySearchQuery("nguyen", SecurityChecksSearchByMode.ALL).size)
    }

    @Test
    fun filteredBySearchQuery_employeeIdMode_onlyMatchesId() {
        val list = listOf(
            sampleSubmission("1", fullName = "No Match Here", employeeId = "E111"),
            sampleSubmission("2", fullName = "Other", employeeId = "E999"),
        )
        val filtered = list.filteredBySearchQuery("e111", SecurityChecksSearchByMode.EMPLOYEE_ID)
        assertEquals(1, filtered.size)
        assertEquals("E111", filtered.first().user?.employeeId)
    }

    @Test
    fun toSecurityCheckItemUi_approvedStatus() {
        val item = sampleSubmission("1", status = "APPROVED").toSecurityCheckItemUi(
            badgeIndex = 3,
            labelApproved = "Approved",
            labelSubmitted = "Submitted",
            labelRejected = "Rejected",
            dash = "—",
        )
        assertEquals(SecurityCheckStatus.APPROVED, item.statusUseApprovedChip)
        assertEquals("Approved", item.statusLabel)
    }

    private fun sampleSubmission(
        idSuffix: String,
        fullName: String = "User $idSuffix",
        employeeId: String = "E$idSuffix",
        status: String = "SUBMITTED",
    ) = SecurityCheckSubmission(
        id = idSuffix.hashCode(),
        templateId = 1,
        userId = 1,
        submittedAt = "2026-04-01T00:00:00.000Z",
        status = status,
        rejectReason = null,
        reviewedBy = null,
        createdAt = "2026-04-01T00:00:00.000Z",
        user = SecurityCheckSubmissionUser(
            id = 1,
            employeeId = employeeId,
            fullName = fullName,
            avatarUrl = null,
        ),
        group = null,
        template = null,
    )
}
