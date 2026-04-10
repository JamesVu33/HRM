package com.example.ihrm.ui.security.checks

import android.util.Log
import com.example.ihrm.R
import com.example.ihrm.domain.model.SecurityCheckSubmission
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun SecurityCheckSubmission.submittedInstant(): Instant? {
    val raw = submittedAt ?: createdAt ?: return null
    return try {
        Instant.parse(raw)
    } catch (_: Exception) {
        null
    }
}

fun List<SecurityCheckSubmission>.filterByDateRange(
    fromMillis: Long?,
    toMillis: Long?,
): List<SecurityCheckSubmission> {
    if (fromMillis == null && toMillis == null) return this
    val zone = ZoneId.systemDefault()
    val fromInstant = fromMillis?.let { ms ->
        Instant.ofEpochMilli(ms).atZone(zone).toLocalDate().atStartOfDay(zone).toInstant()
    }
    val toInstant = toMillis?.let { ms ->
        Instant.ofEpochMilli(ms).atZone(zone).toLocalDate()
            .atTime(23, 59, 59, 999_999_999)
            .atZone(zone)
            .toInstant()
    }
    return filter { submission ->
        val instant = submission.submittedInstant() ?: return@filter false
        if (fromInstant != null && instant.isBefore(fromInstant)) return@filter false
        if (toInstant != null && instant.isAfter(toInstant)) return@filter false
        true
    }
}

fun List<SecurityCheckSubmission>.filterByGroup(groupKey: String?): List<SecurityCheckSubmission> {
    if (groupKey.isNullOrBlank()) return this
    return filter { submission ->
        val g = submission.group ?: return@filter false
        g.id == groupKey || g.code == groupKey
    }
}

private val securityCheckDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .withZone(ZoneId.systemDefault())

private fun formatIsoDate(iso: String?, emptyDisplay: String): String {
    if (iso.isNullOrBlank()) return emptyDisplay
    return try {
        securityCheckDateFormatter.format(Instant.parse(iso))
    } catch (_: Exception) {
        emptyDisplay
    }
}

private fun SecurityCheckSubmission.uiStatus(): SecurityCheckStatus =
    when (status?.uppercase(Locale.US)) {
        "APPROVED" -> SecurityCheckStatus.APPROVED
        "REJECTED" -> SecurityCheckStatus.REJECTED
        "SUBMITTED" -> SecurityCheckStatus.SUBMITTED
        else -> SecurityCheckStatus.NOT_SUBMITTED
    }

/**
 * Map domain submission → card UI. [badgeIndex] hiển thị trên badge tròn (1-based).
 */
fun SecurityCheckSubmission.toSecurityCheckItemUi(
    badgeIndex: Int,
    labelApproved: String,
    labelSubmitted: String,
    labelRejected: String,
    labelNotSubmitted: String,
    dash: String,
): SecurityCheckItemUi {
    val uiStatus = uiStatus()
    val (statusColor, statusBg) = when (uiStatus) {
        SecurityCheckStatus.APPROVED -> figmaApprovedGreen to figmaApprovedGreenBg
        SecurityCheckStatus.REJECTED -> figmaRejectedGreen to figmaRejectedGreenBg
        SecurityCheckStatus.SUBMITTED -> figmaSubmittedBlue to figmaSubmittedBlueBg
        else -> figmaNotSubmittedYellow to figmaNotSubmittedYellowBg
    }
    val statusLabel = when (uiStatus) {
        SecurityCheckStatus.APPROVED -> labelApproved
        SecurityCheckStatus.REJECTED -> labelRejected
        SecurityCheckStatus.SUBMITTED -> labelSubmitted
        else -> labelNotSubmitted
    }
    val approvedByLabelRes = when (uiStatus) {
        SecurityCheckStatus.APPROVED -> R.string.security_checks_approved_by
        SecurityCheckStatus.REJECTED -> R.string.security_checks_rejected_by
        SecurityCheckStatus.SUBMITTED -> R.string.security_checks_wait_for_approve_by
        else -> R.string.security_checks_empty_content
    }
    val approverName = when (uiStatus) {
        SecurityCheckStatus.SUBMITTED -> dash
        else -> reviewedBy?.fullName?.takeIf { it.isNotBlank() } ?: dash
    }
    val secondColumnDate = when (uiStatus) {
        SecurityCheckStatus.SUBMITTED -> dash
        else -> formatIsoDate(
            reviewedBy?.updatedAt ?: reviewedBy?.createdAt ?: createdAt,
            dash,
        )
    }
    val teamName = group?.name?.takeIf { it.isNotBlank() }
        ?: user?.fullName?.takeIf { it.isNotBlank() }
        ?: dash
    val department = buildString {
        val code = group?.code?.takeIf { it.isNotBlank() }
        val tmpl = template?.name?.takeIf { it.isNotBlank() }
        if (code != null) append(code)
        if (code != null && tmpl != null) append(" · ")
        if (tmpl != null) append(tmpl)
    }.ifBlank {
        user?.employeeId?.takeIf { it.isNotBlank() } ?: dash
    }

    val employeeId = user?.employeeId?.takeIf { it.isNotBlank() } ?: dash

    val email = email?.takeIf { it.isNotBlank() } ?: dash
    val phone = phoneNumber?.takeIf { it.isNotBlank() } ?: dash
    val userId = id.toString()
    return SecurityCheckItemUi(
        submissionId = id,
        employeeId = employeeId,
        teamIndex = badgeIndex,
        teamName = teamName,
        department = department,
        statusLabel = statusLabel,
        statusColor = statusColor,
        statusBg = statusBg,
        approvedByLabelRes = approvedByLabelRes,
        approverName = approverName,
        submittedDate = formatIsoDate(submittedAt ?: createdAt, dash),
        approvedDate = secondColumnDate,
        statusUseApprovedChip = uiStatus,
        email = email,
        phoneNumber = phone,
        userId = userId
    )
}

fun List<SecurityCheckSubmission>.toSecurityCheckItemUiList(
    labelApproved: String,
    labelSubmitted: String,
    labelRejected: String,
    dash: String,
    labelNotSubmitted: String,
): List<SecurityCheckItemUi> = mapIndexed { index, submission ->
    submission.toSecurityCheckItemUi(
        badgeIndex = index + 1,
        labelApproved = labelApproved,
        labelSubmitted = labelSubmitted,
        labelRejected = labelRejected,
        labelNotSubmitted = labelNotSubmitted,
        dash = dash,
    )
}

fun List<SecurityCheckSubmission>.filteredBySearchQuery(
    query: String,
    mode: SecurityChecksSearchByMode = SecurityChecksSearchByMode.ALL,
): List<SecurityCheckSubmission> {
    val q = query.trim().lowercase(Locale.getDefault())
    if (q.isEmpty()) return this
    return filter { s ->
        when (mode) {
            SecurityChecksSearchByMode.EMPLOYEE_ID ->
                s.user?.employeeId?.lowercase(Locale.getDefault())?.contains(q) == true
            SecurityChecksSearchByMode.NAME ->
                s.user?.fullNameForSearching?.lowercase(Locale.getDefault())?.contains(q) == true
            SecurityChecksSearchByMode.ALL ->
                sequenceOf(
                    s.user?.fullNameForSearching,
                    s.user?.employeeId,
                    s.group?.name,
                    s.group?.code,
                    s.template?.name,
                    s.status,
                    s.rejectReason,
                ).filterNotNull().any { it.lowercase(Locale.getDefault()).contains(q) }
        }
    }
}
