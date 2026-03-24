package com.example.ihrm.ui.dashboard

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

/**
 * Simple role resolver for Dashboard right after login.
 * `Extra` users are those likely to have management permissions.
 */
internal fun resolveDashboardRoleAfterLogin(
    email: String?,
    fullName: String?
): DashboardRole {
    val key = buildString {
        append(email.orEmpty())
        append(" ")
        append(fullName.orEmpty())
    }.lowercase()

    val extraHints = listOf(
        "admin",
        "manager",
        "lead",
        "hr",
        "s1",
        "s2",
        "extra"
    )
    return if (extraHints.any { key.contains(it) }) DashboardRole.Personal else DashboardRole.Extra
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
