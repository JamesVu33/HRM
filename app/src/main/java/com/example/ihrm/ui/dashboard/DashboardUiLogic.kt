package com.example.ihrm.ui.dashboard

import com.example.ihrm.domain.model.AccountType

enum class DashboardRole {
    Personal,
    Extra
}

data class DashboardPersonalRoleUi(
    val profile: DashboardProfileModel,
    val leaveStats: List<LeaveStatModel>,
    val securityMonthly: SecurityMonthlyModel
)

data class DashboardExtraRoleUi(
    val profile: DashboardProfileModel,
    val leaveStats: List<LeaveStatModel>,
    val securityMonthly: SecurityMonthlyModel,
    val management: DashboardManagementUiModel
)

/**
 * Share of items in the monthly security breakdown.
 */
internal fun calculateShare(
    target: Int,
    values: List<Int>
): Float {
    val total = values.sum()
    return if (total > 0) (target.toFloat() / total).coerceIn(0f, 1f) else 0f
}

/** Rejected slice of the security monthly breakdown (approved / rechecking / rejected). */
internal fun securityRejectedShare(
    approved: Int,
    rechecking: Int,
    rejected: Int
): Float {
    val total = approved + rechecking + rejected
    return if (total > 0) rejected.toFloat() / total else 0f
}

/** Maps login [AccountType] to dashboard shell (tabs / drawer grouping). */
fun AccountType.toDashboardRole(): DashboardRole = when (this) {
    AccountType.Basic -> DashboardRole.Personal
    AccountType.Extra -> DashboardRole.Extra
}

internal fun buildPersonalRoleUi(source: DashboardHomeMockModel): DashboardPersonalRoleUi {
    return DashboardPersonalRoleUi(
        profile = source.profile,
        leaveStats = source.leaveStats,
        securityMonthly = source.securityMonthly
    )
}

internal fun buildExtraRoleUi(source: DashboardHomeMockModel): DashboardExtraRoleUi {
    return DashboardExtraRoleUi(
        profile = source.profile,
        leaveStats = source.leaveStats,
        securityMonthly = source.securityMonthly,
        management = source.management
    )
}
