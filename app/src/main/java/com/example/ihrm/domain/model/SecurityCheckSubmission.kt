package com.example.ihrm.domain.model

data class SecurityCheckSubmissionsPage(
    val items: List<SecurityCheckSubmission>,
    val meta: SubmissionPaginationMeta?,
)

data class SubmissionPaginationMeta(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int,
)

data class SecurityCheckSubmission(
    val id: Int,
    val templateId: Int,
    val userId: Int,
    val submittedAt: String?,
    val status: String?,
    val rejectReason: String?,
    val reviewedBy: SecurityCheckReviewer?,
    val createdAt: String?,
    val user: SecurityCheckSubmissionUser?,
    val group: SecurityCheckGroup?,
    val template: SecurityCheckTemplate?,
    val employeeId: String?,
)

data class SecurityCheckSubmissionUser(
    val id: Int,
    val employeeId: String?,
    val fullName: String?,
    val avatarUrl: String?,
)

data class SecurityCheckReviewer(
    val id: Int,
    val employeeId: String?,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val status: String?,
    val isSystem: Boolean?,
    val createdAt: String?,
    val updatedAt: String?,
)

data class SecurityCheckTemplate(
    val id: Int,
    val name: String?,
)

data class SecurityCheckGroup(
    val id: String?,
    val code: String?,
    val name: String?,
    val description: String?,
    val path: String?,
    val leaderId: Int?,
)
