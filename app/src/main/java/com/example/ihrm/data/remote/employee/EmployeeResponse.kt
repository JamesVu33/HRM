package com.example.ihrm.data.remote.employee

data class EmployeeResponse(
    val id: Int,
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val status: String,
    val title: Title,
    val level: Level,
    val certifications: List<Certification>,
    val educations: List<Education>,
    val profile: Profile?,
    val membershipOf: List<Membership>,
    val createdAt: String,
    val updatedAt: String
)

data class Title(
    val id: Int,
    val code: String,
    val name: String,
    val description: String
)

data class Level(
    val id: Int,
    val code: String,
    val name: String,
    val description: String
)

data class Certification(
    val id: Int,
    val name: String,
    val issuer: String,
    val issueDate: String,
    val expiryDate: String?,
    val url: String?,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String
)

data class Education(
    val id: Int? = null
    // todo thêm field sau nếu API update
)

data class Profile(
    val avatarUrl: String?,
    val dateOfBirth: String?,
    val ethnic: String?,
    val religion: String?,
    val identityIdExpiryDate: String?,
    val placeOfBirth: String?,
    val certifications: List<Certification>?,
    val educations: List<Education>?
)

data class Membership(
    val id: String,
    val code: String,
    val name: String
)