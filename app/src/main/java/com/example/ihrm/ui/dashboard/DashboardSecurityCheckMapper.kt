package com.example.ihrm.ui.dashboard

import com.example.ihrm.data.remote.securities.SecurityCheckDashboardResponse
import com.example.ihrm.data.remote.securities.StatusCountDto
import java.util.Locale

/**
 * Approved total from [SecurityCheckDashboardResponse.statsThisMonth] (status APPROVED).
 */
internal fun approvedCountFromStatsThisMonth(stats: List<StatusCountDto>): Int =
    stats.sumOf { dto ->
        if (dto.status.equals("APPROVED", ignoreCase = true)) dto.count ?: 0 else 0
    }

/**
 * Maps per-status counts for the current month into [SecurityMonthlyModel]
 * (Approved / Re-checking = SUBMITTED / Rejected).
 */
internal fun mapStatsThisMonthToMonthly(stats: List<StatusCountDto>): SecurityMonthlyModel {
    var approved = 0
    var submitted = 0
    var rejected = 0
    for (dto in stats) {
        val n = dto.count ?: 0
        when (dto.status?.uppercase(Locale.US)) {
            "APPROVED" -> approved += n
            "SUBMITTED" -> submitted += n
            "REJECTED" -> rejected += n
        }
    }
    return SecurityMonthlyModel(
        approved = approved,
        rechecking = submitted,
        rejected = rejected
    )
}

/**
 * Pending for management overview: max(0, submittedCount − approvedCount), aligned with iOS.
 */
internal fun managementPendingCount(submittedCount: Int, approvedCount: Int): Int =
    maxOf(0, submittedCount - approvedCount)

internal fun mapSecurityCheckDashboardToManagementSecurity(
    response: SecurityCheckDashboardResponse
): ManagementSecurityUiModel {
    val summary = response.submissionSummary
    val submittedCount = summary?.submittedCount ?: 0
    val approved = approvedCountFromStatsThisMonth(response.statsThisMonth)
    val pending = managementPendingCount(submittedCount, approved)
    return ManagementSecurityUiModel(
        totalUsers = summary?.totalUsers ?: 0,
        submissionsCount = summary?.totalSubmissions ?: response.totalSubmissions ?: 0,
        submitted = submittedCount,
        pending = pending,
        notSubmitted = summary?.notSubmittedCount ?: 0
    )
}

internal fun mapSecurityCheckDashboardToCardState(
    response: SecurityCheckDashboardResponse,
    isLoading: Boolean,
    errorMessage: String?
): DashboardSecurityCardState {
    val monthly = mapStatsThisMonthToMonthly(response.statsThisMonth)
    val counts = SecuritySubmissionCounts(
        total = response.submissionSummary?.totalSubmissions
            ?: response.totalSubmissions
            ?: (monthly.approved + monthly.rechecking + monthly.rejected),
        approved = monthly.approved,
        rejected = monthly.rejected,
        submitted = monthly.rechecking,
        pending = 0
    )
    return DashboardSecurityCardState(
        isLoading = isLoading,
        monthly = monthly,
        banner = resolveDashboardSecurityBanner(counts, isLoading, errorMessage)
    )
}
