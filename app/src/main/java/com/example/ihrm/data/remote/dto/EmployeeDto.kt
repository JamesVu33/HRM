package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EmployeeDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("department")
    val department: String?,
    @SerializedName("position")
    val position: String?,
    @SerializedName("hireDate")
    val hireDate: String?,
    @SerializedName("salary")
    val salary: Double?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("englishName")
    val englishName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("personalId")
    val personalId: String? = null,
    @SerializedName("idIssueDate")
    val idIssueDate: String? = null,
    @SerializedName("levelId")
    val levelId: Int? = null,
    @SerializedName("level")
    val level: LevelShortDto? = null,
    /** ISO-8601 from API (e.g. `2026-04-01T02:26:02.728Z`); not a Unix ms number. */
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)