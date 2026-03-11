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
    @SerializedName("createdAt")
    val createdAt: Long?,
    @SerializedName("updatedAt")
    val updatedAt: Long?
)