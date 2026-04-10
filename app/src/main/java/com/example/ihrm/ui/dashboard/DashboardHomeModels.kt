package com.example.ihrm.ui.dashboard

import androidx.annotation.StringRes
import com.example.ihrm.domain.model.Employee

data class DashboardProfileModel(
    val roleName: String?,
    val displayName: String,
    val employeeId: String,
    val departmentBadge: String,
    val email: String,
    val phone: String,
    val joined: String,
    val departmentDetail: String,
    /** Remote avatar; when null or blank, [avatarInitials] is shown (see [com.example.ihrm.ui.common.Avatar]). */
    val avatarUrl: String? = null,
    /** Fallback letters when [avatarUrl] is missing. */
    val avatarInitials: String,
    val avatarSecurity: String? = null
)

data class LeaveStatModel(
    val value: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val accent: LeaveStatAccent
)

enum class LeaveStatAccent {
    Blue,
    Red,
    Amber,
    Green
}

data class SecurityMonthlyModel(
    val approved: Int,
    val rechecking: Int,
    val rejected: Int
)

/** Banner copy for security cards (aligned with iOS dashboard security banner). */
data class DashboardSecurityBannerModel(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    /** When set (e.g. API error), shown instead of [subtitleRes]. */
    val subtitleOverride: String? = null
)

data class DashboardSecurityCardState(
    val isLoading: Boolean,
    val monthly: SecurityMonthlyModel,
    val banner: DashboardSecurityBannerModel
)

data class DashboardHomeMockModel(
    val profile: DashboardProfileModel,
    val leaveStats: List<LeaveStatModel>,
    val securityMonthly: SecurityMonthlyModel,
    /** Management tab: calendar + security overview (Figma 871:35029). */
    val management: DashboardManagementUiModel
)

/** Mock / API-ready model for the Dashboard Management tab. */
data class DashboardManagementUiModel(
    val calendar: ManagementCalendarUiModel,
    val security: ManagementSecurityUiModel
)

data class ManagementCalendarUiModel(
    val presentCount: Int,
    val absentCount: Int,
    val totalHeadcount: Int,
    val attendanceRatePercent: Int
)

data class ManagementSecurityUiModel(
    val totalUsers: Int,
    val submissionsCount: Int,
    val submitted: Int,
    val pending: Int,
    val notSubmitted: Int
)
