package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserShortDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)
