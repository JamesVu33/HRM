package com.example.ihrm.data.remote.login

import com.google.gson.annotations.SerializedName

data class PermissionRequest(
    @SerializedName("roleId")
    val employeeId: Int,
)