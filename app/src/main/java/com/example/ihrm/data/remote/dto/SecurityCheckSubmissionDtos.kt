package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SecurityCheckSubmissionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("templateId")
    val templateId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("submittedAt")
    val submittedAt: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("rejectReason")
    val rejectReason: String? = null,
    @SerializedName("reviewedBy")
    val reviewedBy: SecurityCheckReviewerDto? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("user")
    val user: SecurityCheckSubmissionUserDto? = null,
    @SerializedName("group")
    val group: SecurityCheckGroupDto? = null,
    @SerializedName("template")
    val template: SecurityCheckTemplateDto? = null,
    @SerializedName("employeeId")
    val employeeId: String? = null,
)

data class SecurityCheckSubmissionUserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("employeeId")
    val employeeId: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("profile")
    val profile: ProfileAvatarResponseDto? = null,
)

data class SecurityCheckReviewerDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("employeeId")
    val employeeId: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("isSystem")
    val isSystem: Boolean? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
)

data class SecurityCheckTemplateDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String? = null,
)

data class SecurityCheckGroupDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("leaderId")
    val leaderId: Int? = null,
)
