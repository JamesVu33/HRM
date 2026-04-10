package com.example.ihrm.data.remote.organizationresponse

import com.google.gson.annotations.SerializedName

data class OrganizationResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("parentId")
    val parentId: String? = null,

    @SerializedName("order")
    val order: Int? = null,

    @SerializedName("requireSecurityCheck")
    val requireSecurityCheck: Boolean? = null,

    @SerializedName("leader")
    val leader: EmployeeDepartmentResponse? = null,

    @SerializedName("members")
    val members: List<EmployeeDepartmentResponse>? = null,

    @SerializedName("children")
    val children: List<String>? = null,

    @SerializedName("memberCount")
    val memberCount: Int? = null
)

data class EmployeeDepartmentResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("employeeId")
    val employeeId: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("level")
    val title: Title? = null,

    @SerializedName("level")
    val level: Level? = null,

    @SerializedName("roles")
    val roles: List<Role>? = null,

    @SerializedName("avatarUrl")
    val avatarUrl: String? = null
)

data class Title(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class Level(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class Role(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null,

    @SerializedName("isSystem")
    val isSystem: Boolean? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("deletedAt")
    val deletedAt: String? = null,

    @SerializedName("deletedById")
    val deletedById: Int? = null
)