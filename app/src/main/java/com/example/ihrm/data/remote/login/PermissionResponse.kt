package com.example.ihrm.data.remote.login

import com.google.gson.annotations.SerializedName

data class PermissionResponse(
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("permissions")
    val permissions: Map<String, PermissionDto>
)

data class PermissionDto(
    @SerializedName("canRead")
    val canRead: Boolean,
    @SerializedName("canCreate")
    val canCreate: Boolean,
    @SerializedName("canUpdate")
    val canUpdate: Boolean,
    @SerializedName("canDelete")
    val canDelete: Boolean
)