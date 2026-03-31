package com.example.ihrm.data.remote.base

import com.example.ihrm.data.remote.dto.MetaDto
import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper from be-nest-hrm backend.
 */
data class ApiSuccessResponse<T>(
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("meta")
    val meta: MetaDto? = null
)