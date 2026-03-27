package com.example.ihrm.data.remote.login

import com.example.ihrm.data.remote.dto.RoleShortDto
import com.example.ihrm.data.remote.dto.UserShortDto
import com.google.gson.annotations.SerializedName

/**
 * Response data for successful login (nested inside ApiResponse.data).
 */
data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("accessTokenExp")
    val accessTokenExp: Long,
    @SerializedName("refreshTokenExp")
    val refreshTokenExp: Long,
    @SerializedName("user")
    val user: UserShortDto,
    @SerializedName("roles")
    val roles: List<RoleShortDto>
)