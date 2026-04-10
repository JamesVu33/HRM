package com.example.ihrm.ui.dashboard

import com.example.ihrm.R
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import java.util.Locale

/**
 * Counts derived from self-submissions list for dashboard security UI (banner + monthly bars).
 */
internal data class SecuritySubmissionCounts(
    val total: Int,
    val approved: Int,
    val rejected: Int,
    val submitted: Int,
    val pending: Int
) {
    /** Shown as "Re-checking" in [SecurityMonthlyModel]. */
    val rechecking: Int get() = pending + submitted
}

internal fun aggregateSecuritySubmissions(list: List<MySecurityCheckResponse>): SecuritySubmissionCounts {
    var approved = 0
    var rejected = 0
    var submitted = 0
    var pending = 0
    for (item in list) {
        when (item.status?.lowercase(Locale.US)) {
            "approved" -> approved++
            "rejected" -> rejected++
            "submitted" -> submitted++
            "pending" -> pending++
        }
    }
    return SecuritySubmissionCounts(
        total = list.size,
        approved = approved,
        rejected = rejected,
        submitted = submitted,
        pending = pending
    )
}

/**
 * Resolves banner level/subtitle in the same order as the iOS dashboard security banner.
 */
internal fun resolveDashboardSecurityBanner(
    counts: SecuritySubmissionCounts,
    isLoading: Boolean,
    errorMessage: String?
): DashboardSecurityBannerModel {
    val err = errorMessage?.takeIf { it.isNotBlank() }
    if (err != null) {
        return DashboardSecurityBannerModel(
            titleRes = R.string.dashboard_security_banner_error_title,
            subtitleRes = R.string.dashboard_security_banner_default_subtitle,
            subtitleOverride = err
        )
    }
    if (counts.submitted > 0) {
        return DashboardSecurityBannerModel(
            titleRes = R.string.dashboard_security_banner_review_title,
            subtitleRes = R.string.dashboard_security_banner_review_subtitle
        )
    }
    if (counts.rejected > 0 && counts.approved == 0 && counts.submitted == 0) {
        return DashboardSecurityBannerModel(
            titleRes = R.string.dashboard_security_banner_action_title,
            subtitleRes = R.string.dashboard_security_banner_rejected_subtitle
        )
    }
    if (counts.total == 0 && !isLoading) {
        return DashboardSecurityBannerModel(
            titleRes = R.string.dashboard_security_banner_empty_title,
            subtitleRes = R.string.dashboard_security_banner_empty_subtitle
        )
    }
    // Subtitle-only branch on iOS: rejected > 0 uses resubmit copy even when level is "All caught up"
    // (e.g. some approved, still have rejections).
    if (counts.rejected > 0) {
        return DashboardSecurityBannerModel(
            titleRes = R.string.dashboard_security_banner_all_good_title,
            subtitleRes = R.string.dashboard_security_banner_rejected_subtitle
        )
    }
    return DashboardSecurityBannerModel(
        titleRes = R.string.dashboard_security_banner_all_good_title,
        subtitleRes = R.string.dashboard_security_banner_default_subtitle
    )
}

internal fun buildDashboardSecurityCardState(
    counts: SecuritySubmissionCounts,
    isLoading: Boolean,
    errorMessage: String?
): DashboardSecurityCardState {
    val monthly = SecurityMonthlyModel(
        approved = counts.approved,
        rechecking = counts.rechecking,
        rejected = counts.rejected
    )
    return DashboardSecurityCardState(
        isLoading = isLoading,
        monthly = monthly,
        banner = resolveDashboardSecurityBanner(counts, isLoading, errorMessage)
    )
}
