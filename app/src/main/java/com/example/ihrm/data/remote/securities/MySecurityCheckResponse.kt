package com.example.ihrm.data.remote.securities

import com.google.gson.annotations.SerializedName

data class MySecurityCheckResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("templateId")
    val templateId: Int? = null,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("submittedAt")
    val submittedAt: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("rejectReason")
    val rejectReason: String? = null,

    @SerializedName("answers")
    val answers: List<Answer>? = null,

    @SerializedName("reviewedBy")
    val reviewedBy: Reviewer? = null,

    @SerializedName("user")
    val user: UserProfile? = null,

    @SerializedName("template")
    val template: TemplateInfo? = null,
)

data class Answer(
    @SerializedName("key")
    val key: String? = null,

    @SerializedName("value")
    val value: Boolean? = null,

    @SerializedName("remark")
    val remark: String? = null,
)

data class Reviewer(
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

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("isSystem")
    val isSystem: Boolean? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
)

data class UserProfile(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("employeeId")
    val employeeId: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("profile")
    val profile: ProfileDetail? = null,

    @SerializedName("membersOf")
    val membersOf: List<String> = emptyList()
)

data class ProfileDetail(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
)

data class TemplateInfo(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val templateName: String? = null
)