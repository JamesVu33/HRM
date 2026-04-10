package com.example.ihrm.domain.usecase.employees

import com.google.gson.annotations.SerializedName

data class EmployeeListDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("employeeId") val employeeId: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("title") val title: TitleDto? = null,
    @SerializedName("level") val level: LevelDto? = null,
    @SerializedName("certifications") val certifications: List<CertificationDto>? = null,
    @SerializedName("educations") val educations: List<EducationDto>? = null,
    @SerializedName("profile") val profile: ProfileDto? = null,
    @SerializedName("membershipOf") val membershipOf: List<MembershipDto>? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class TitleDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)

data class LevelDto(
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
    @SerializedName("url") val url: String? = null
)

data class EducationDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("schoolName") val schoolName: String? = null,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("major") val major: String? = null,
    @SerializedName("graduationDate") val graduationDate: String? = null
)

data class ProfileDto(
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("ethnic") val ethnic: String? = null,
    @SerializedName("religion") val religion: String? = null,
    @SerializedName("identityIdExpiryDate") val identityIdExpiryDate: String? = null,
    @SerializedName("placeOfBirth") val placeOfBirth: String? = null
)

data class MembershipDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("name") val name: String? = null
)
