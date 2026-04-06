package com.example.ihrm.domain.model

import com.example.ihrm.data.remote.dto.SubmissionByStatus

data class SecurityCheckSubmissionsPage(
    val items: List<SecurityCheckSubmission>,
    val listStats: List<SubmissionByStatus>,
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
    val email: String? = null,
    val phoneNumber: String? = null,
    val submittedAt: String? = null,
    val status: String? = null,
    val rejectReason: String? = null,
    val reviewedBy: SecurityCheckReviewer? = null,
    val createdAt: String? = null,
    val user: SecurityCheckSubmissionUser? = null,
    val group: SecurityCheckGroup? = null,
    val template: SecurityCheckTemplate? = null,
    val employeeId: String? = null,
)

data class SecurityCheckSubmissionUser(
    val id: Int,
    val employeeId: String?,
    val fullName: String?,
    val avatarUrl: String?,
    val fullNameForSearching: String?,
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
