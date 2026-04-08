package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Current user's employee info from GET /me/employee-info.
 */
data class MeEmployeeResponse(
    @SerializedName("status")
    val status: String? = null,

    @SerializedName("onBoardAt")
    val onBoardAt: String? = null,

    @SerializedName("resignedAt")
    val resignedAt: String? = null,

    @SerializedName("title")
    val title: TitleShortDto? = null,

    @SerializedName("level")
    val level: LevelShortDto? = null,

    @SerializedName("roles")
    val roles: List<RoleShortDto> = emptyList(),

    @SerializedName("groups")
    val groups: List<Any> = emptyList(),

    @SerializedName("certifications")
    val certifications: List<CertificationDto> = emptyList(),

    @SerializedName("educations")
    val educations: List<Any> = emptyList()
)

data class CertificationDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("issuer")
    val issuer: String? = null,
    @SerializedName("issueDate")
    val issueDate: String? = null,
    @SerializedName("expiryDate")
    val expiryDate: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
