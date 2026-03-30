package com.example.ihrm.data.remote.dto

data class MyInfoDto(
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val id: Int,
    val roles: List<RoleShortDto>,
    val status: String,
    val profile: ProfileDto?,
    val settings: SettingsDto?
)

data class ProfileDto(
    val avatarUrl: String?
)

data class SettingsDto(
    val lang: String,
    val theme: String
)
