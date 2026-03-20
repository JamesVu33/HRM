package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /auth/login.
 */
data class LoginDto(
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("password")
    val password: String
)
