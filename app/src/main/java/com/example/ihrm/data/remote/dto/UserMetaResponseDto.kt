package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response data from GET /employees/meta (roles, titles, levels lookup).
 */
data class UserMetaResponseDto(
    @SerializedName("roles")
    val roles: List<RoleShortDto>,
    @SerializedName("titles")
    val titles: List<TitleShortDto>,
    @SerializedName("levels")
    val levels: List<LevelShortDto>
)
