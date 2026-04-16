package com.example.ihrm.domain.model.securitycheck

import com.example.ihrm.data.remote.securities.SubmissionStatusResponse

enum class SubmissionStatus {
    SUBMITTED,
    APPROVED,
    REJECTED,
    NOT_SUBMITTED,
    UNKNOWN
}

data class SubmissionStat(
    val status: SubmissionStatus,
    val count: Int
)

fun SubmissionStatusResponse.toDomain(): SubmissionStat {
    val domainStatus = when (this.status?.uppercase()) {
        "SUBMITTED" -> SubmissionStatus.SUBMITTED
        "APPROVED" -> SubmissionStatus.APPROVED
        "REJECTED" -> SubmissionStatus.REJECTED
        "NOT_SUBMITTED" -> SubmissionStatus.NOT_SUBMITTED
        else -> SubmissionStatus.UNKNOWN
    }
    return SubmissionStat(
        status = domainStatus,
        count = this.count ?: 0
    )
}