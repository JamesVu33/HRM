package com.example.ihrm.ui.dashboard

import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.util.Constants.DASH
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Maps GET /me/employee-info plus session fields into [DashboardProfileModel].
 * [fallback] supplies strings when API/session omit values (e.g. before first successful load).
 */
internal fun mapMeEmployeeToDashboardProfile(
    me: MeEmployeeResponse,
    fallback: DashboardProfileModel,
    displayName: String?,
    email: String?,
    employeeId: String?,
    phone: String?
): DashboardProfileModel {
    val name = displayName?.takeIf { it.isNotBlank() } ?: fallback.displayName
    val empId = employeeId?.takeIf { it.isNotBlank() } ?: fallback.employeeId
    val emailFinal = email?.takeIf { it.isNotBlank() } ?: fallback.email
    val phoneFinal = phone?.takeIf { !it.isNullOrBlank() } ?: fallback.phone

    val titleName = me.title?.name?.takeIf { it.isNotBlank() }
    val roleName = me.roles.firstOrNull()?.name?.takeIf { it.isNotBlank() }
    val badge = titleName ?: roleName ?: fallback.departmentBadge

    val levelPart = me.level?.name?.takeIf { it.isNotBlank() }
        ?: me.level?.code?.takeIf { it.isNotBlank() }
        ?: ""
    val departmentDetail = when {
        levelPart.isNotBlank() -> levelPart
        else -> DASH
    }

    val joined = formatOnBoardAt(me.onBoardAt) ?: fallback.joined

    return DashboardProfileModel(
        displayName = name,
        employeeId = empId,
        departmentBadge = badge,
        email = emailFinal,
        phone = phoneFinal,
        joined = joined,
        departmentDetail = departmentDetail,
        avatarUrl = fallback.avatarUrl,
        avatarInitials = DashboardMockData.avatarInitialsFromDisplayName(name),
        avatarSecurity = fallback.avatarSecurity
    )
}

internal fun formatOnBoardAt(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    val trimmed = raw.trim()
    return try {
        val datePart = if (trimmed.length >= 10) trimmed.take(10) else trimmed
        val date = LocalDate.parse(datePart)
        date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
    } catch (_: Exception) {
        trimmed
    }
}
