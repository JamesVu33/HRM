package com.example.ihrm.data.remote.login

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /auth/login.
 */
data class LoginRequest(
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("password")
    val password: String
) {
    companion object {
        fun isEmployeeIdField(errorKey: String) = errorKey == "employeeId"
        fun isPasswordField(errorKey: String) = errorKey == "password"
    }
}