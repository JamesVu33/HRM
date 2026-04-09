package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EmployeeDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("employeeId") val employeeId: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("isLeader") val isLeader: Boolean? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,

    @SerializedName("profile") val profile: ProfileDto? = null,
    @SerializedName("title") val title: TitleLevelDto? = null,
    @SerializedName("level") val level: TitleLevelDto? = null,
    @SerializedName("roles") val roles: RoleShortDto? = null,

    @SerializedName("leaderOf") val leaderOf: List<DepartmentDto>? = null,
    @SerializedName("membershipOf") val membershipOf: List<DepartmentDto>? = null,

    @SerializedName("certifications") val certifications: List<CertificationDto>? = null,

    @SerializedName("educations") val educations: List<CertificationDto>? = null
)

data class ProfileDto(
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("identityIdExpiryDate") val identityIdExpiryDate: String? = null,
    @SerializedName("ethnic") val ethnic: String? = null,
    @SerializedName("religion") val religion: String? = null,
    @SerializedName("placeOfBirth") val placeOfBirth: String? = null,
    @SerializedName("certifications") val certifications: List<CertificationDto>? = null,
    @SerializedName("educations") val educations: List<EducationDetailDto>? = null
)

data class TitleLevelDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)

data class DepartmentDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("name") val name: String? = null
)

data class EducationDetailDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("schoolName") val schoolName: String? = null,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("major") val major: String? = null,
    @SerializedName("graduationDate") val graduationDate: String? = null,
    @SerializedName("userId") val userId: Int? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class CertificationDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("issuer") val issuer: String? = null,
    @SerializedName("issueDate") val issueDate: String? = null,
    @SerializedName("expiryDate") val expiryDate: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("userId") val userId: Int? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)