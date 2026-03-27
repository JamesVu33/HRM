package com.example.ihrm.data.remote.base

import com.example.ihrm.data.remote.dto.MetaDto
import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper from be-nest-hrm backend.
 */
data class ApiSuccessResponse<T>(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T?,
    @SerializedName("meta")
    val meta: MetaDto? = null
)