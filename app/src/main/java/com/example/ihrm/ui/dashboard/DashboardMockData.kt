package com.example.ihrm.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.ihrm.R
import com.example.ihrm.domain.model.Employee

/**
 * Mock dashboard payload aligned with Figma HRM dashboard (Personal + Management).
 */
object DashboardMockData {

    @Composable
    fun rememberHomeModel(): DashboardHomeMockModel {
        val name = stringResource(R.string.dashboard_mock_profile_name)
        val id = stringResource(R.string.dashboard_mock_profile_id)
        val dept = stringResource(R.string.dashboard_mock_department)
        val email = stringResource(R.string.dashboard_mock_email)
        val phone = stringResource(R.string.dashboard_mock_phone)
        val joined = stringResource(R.string.dashboard_mock_joined)
        return remember(name, id, dept, email, phone, joined) {
            DashboardHomeMockModel(
                profile = DashboardProfileModel(
                    displayName = name,
                    employeeId = id,
                    departmentBadge = dept,
                    email = email,
                    phone = phone,
                    joined = joined,
                    departmentDetail = dept,
                    avatarUrl = null,
                    avatarInitials = avatarInitialsFromDisplayName(name),
                    avatarSecurity = null
                ),
                leaveStats = listOf(
                    LeaveStatModel(
                        value = 22,
                        titleRes = R.string.dashboard_leave_total_title,
                        descriptionRes = R.string.dashboard_leave_total_desc,
                        accent = LeaveStatAccent.Blue
                    ),
                    LeaveStatModel(
                        value = 3,
                        titleRes = R.string.dashboard_leave_used_title,
                        descriptionRes = R.string.dashboard_leave_used_desc,
                        accent = LeaveStatAccent.Red
                    ),
                    LeaveStatModel(
                        value = 1,
                        titleRes = R.string.dashboard_leave_pending_title,
                        descriptionRes = R.string.dashboard_leave_pending_desc,
                        accent = LeaveStatAccent.Amber
                    ),
                    LeaveStatModel(
                        value = 10,
                        titleRes = R.string.dashboard_leave_remaining_title,
                        descriptionRes = R.string.dashboard_leave_remaining_desc,
                        accent = LeaveStatAccent.Green
                    )
                ),
                securityMonthly = SecurityMonthlyModel(
                    approved = 0,
                    rechecking = 1,
                    rejected = 3
                ),
                management = DashboardManagementUiModel(
                    calendar = ManagementCalendarUiModel(
                        dayOfMonth = "19",
                        monthLabel = "Mar",
                        weekdayLabel = "Thu",
                        presentCount = 38,
                        absentCount = 7,
                        totalHeadcount = 45,
                        attendanceRatePercent = 84
                    ),
                    security = ManagementSecurityUiModel(
                        totalUsers = 45,
                        submissionsCount = 20,
                        submitted = 20,
                        pending = 5,
                        notSubmitted = 20
                    )
                )
            )
        }
    }

    /** Up to two initials from the first tokens of the display name (e.g. "Tiểu Long Nữ" → "TL"). */
    internal fun avatarInitialsFromDisplayName(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.size >= 2 ->
                "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
            parts.isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "?"
        }
    }
}
