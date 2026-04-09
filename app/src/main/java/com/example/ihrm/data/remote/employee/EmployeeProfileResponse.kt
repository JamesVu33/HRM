package com.example.ihrm.data.remote.employee

import com.example.ihrm.data.remote.dto.RoleShortDto
import com.google.gson.annotations.SerializedName
data class EmployeeProfileResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("englishName") val englishName: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("maritalStatus") val maritalStatus: String? = null,
    @SerializedName("nationality") val nationality: String? = null,
    @SerializedName("ethnic") val ethnic: String? = null,
    @SerializedName("religion") val religion: String? = null,
    @SerializedName("placeOfBirth") val placeOfBirth: String? = null,

    @SerializedName("identityId") val identityId: String? = null,
    @SerializedName("identityIdIssueDate") val identityIdIssueDate: String? = null,
    @SerializedName("identityIdIssuePlace") val identityIdIssuePlace: String? = null,
    @SerializedName("identityIdExpiryDate") val identityIdExpiryDate: String? = null,

    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,

    @SerializedName("level") val level: TitleLevelDto? = null,
    @SerializedName("title") val title: TitleLevelDto? = null,
    @SerializedName("roles") val roles: RoleShortDto? = null,

    @SerializedName("certifications") val certifications: List<CertificationDto>? = null,
    @SerializedName("educations") val educations: List<EducationDetailDto>? = null
)

data class TitleLevelDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)

data class CertificationDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("issuer") val issuer: String? = null,
    @SerializedName("issueDate") val issueDate: String? = null,
    @SerializedName("expiryDate") val expiryDate: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("deletedAt") val deletedAt: String? = null
)

data class EducationDetailDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("schoolName") val schoolName: String? = null
)