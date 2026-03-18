package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Current user's employee info from GET /me/employee-info.
 */
data class MeEmployeeResponse(
    @SerializedName("level")
    val level: LevelShortDto? = null,
    @SerializedName("title")
    val title: TitleShortDto? = null,
    @SerializedName("roles")
    val roles: List<RoleShortDto>? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("onBoardAt")
    val onBoardAt: String? = null,
    @SerializedName("resignedAt")
    val resignedAt: String? = null,
    @SerializedName("groups")
    val groups: List<Any>? = null
)
