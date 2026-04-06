package com.example.ihrm.ui.security.checks

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

enum class SecurityCheckStatus {
    APPROVED, SUBMITTED, REJECTED, NOT_SUBMITTED
}

data class SecurityCheckItemUi(
    /** Id bản ghi API; dùng làm key LazyColumn. */
    val submissionId: Int = 0,
    val employeeId: String = "",
    val teamIndex: Int,
    val teamName: String,
    val department: String,
    val statusLabel: String,
    val statusColor: Color,
    val statusBg: Color,
    /** When non-null, shows grey label + name; when null, single line for [approverName]. */
    @StringRes val approvedByLabelRes: Int?,
    val approverName: String,
    val submittedDate: String,
    val approvedDate: String,
    val statusUseApprovedChip: SecurityCheckStatus = SecurityCheckStatus.APPROVED,
    val email: String,
    val phoneNumber: String
)

internal val figmaApprovedGreen = Color(0xFF34C759)
internal val figmaApprovedGreenBg = Color(0x1F34C759)
internal val figmaRejectedGreen = Color(0xFFF44336)
internal val figmaRejectedGreenBg = Color(0x21F44336)
internal val figmaSubmittedBlue = Color(0xFF007AFF)
internal val figmaSubmittedBlueBg = Color(0x1F007AFF)
internal val figmaNotSubmittedYellow = Color(0xFFF0B100)
internal val figmaNotSubmittedYellowBg = Color(0xFFFFF5D7)