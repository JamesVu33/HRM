package com.example.ihrm.ui.security.checks

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.ihrm.R

enum class SecurityCheckStatus {
    APPROVED, SUBMITTED, REJECTED
}

data class SecurityCheckItemUi(
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
    val statusUseApprovedChip: SecurityCheckStatus = SecurityCheckStatus.APPROVED
)

data class SecurityQuestionItem(
    val id: Int,
    val content: String,
)

private val figmaApprovedGreen = Color(0xFF34C759)
private val figmaApprovedGreenBg = Color(0x1F34C759)
private val figmaRejectedGreen = Color(0xFFF44336)
private val figmaRejectedGreenBg = Color(0x21F44336)
private val figmaSubmittedBlue = Color(0xFF007AFF)
private val figmaSubmittedBlueBg = Color(0x1F007AFF)

val demoChecks = listOf(
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Approved",
        statusColor = figmaApprovedGreen,
        statusBg = figmaApprovedGreenBg,
        approvedByLabelRes = R.string.security_checks_approved_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.APPROVED
    ),
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Approved",
        statusColor = figmaApprovedGreen,
        statusBg = figmaApprovedGreenBg,
        approvedByLabelRes = R.string.security_checks_approved_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.APPROVED
    ),
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Rejected",
        statusColor = figmaRejectedGreen,
        statusBg = figmaRejectedGreenBg,
        approvedByLabelRes = R.string.security_checks_rejected_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.REJECTED
    ),
    SecurityCheckItemUi(
        teamIndex = 8,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Submitted",
        statusColor = figmaSubmittedBlue,
        statusBg = figmaSubmittedBlueBg,
        approvedByLabelRes = R.string.security_checks_wait_for_approve_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "-",
        statusUseApprovedChip = SecurityCheckStatus.SUBMITTED
    )
)

fun SecurityCheckStatus.toLegendKey(): String = when (this) {
    SecurityCheckStatus.APPROVED -> "approved"
    SecurityCheckStatus.SUBMITTED -> "submitted"
    SecurityCheckStatus.REJECTED -> "rejected"
}

fun securityQuestionsByLegend(label: String): List<SecurityQuestionItem> {
    val base = listOf(
        "Are terminals (Notebooks, Desktop PCs, etc.) locked with the locks?",
        "There are no information (ID, PW, etc.) leakage written on post-it notes and attached to open places (like monitors desks)?",
        "Are information (ID, PW, etc.) leakage safely managed without being stored on the PC or department's web hard?",
        "Are confidential documents (including personal information) or print-outs containing work-related information stored in lockable drawers or cabinets?",
        "Are confidential information prohibited to print-outs?",
        "Do you use a paper shredder to destroy confidential documents instead of throwing them away?",
        "Do you keep the lock key on your desk or office with careful attention?",
        "Are you locking desk drawers and cabinets when you leave work?"
    )
    return base.mapIndexed { index, text ->
        SecurityQuestionItem(id = index + 1, content = text)
    }
}
