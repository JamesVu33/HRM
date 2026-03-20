package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * User/employee item from GET /employees (be-nest-hrm).
 */
data class UserResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("roles")
    val roles: List<RoleShortDto>? = null,
    @SerializedName("levelId")
    val levelId: Int? = null,
    @SerializedName("level")
    val level: LevelShortDto? = null,
    @SerializedName("title")
    val title: TitleShortDto? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("profile")
    val profile: ProfileAvatarResponseDto? = null
)

data class ProfileAvatarResponseDto(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null
)
