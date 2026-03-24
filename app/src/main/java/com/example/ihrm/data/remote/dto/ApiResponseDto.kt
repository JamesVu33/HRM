package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper from be-nest-hrm backend.
 */
data class ApiResponseDto<T>(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?,
    @SerializedName("meta")
    val meta: MetaDto? = null
)
