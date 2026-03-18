package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Error response body from be-nest-hrm (4xx/5xx).
 */
data class AppErrorResponseDto(
    @SerializedName("errorType")
    val errorType: String,
    @SerializedName("errors")
    val errors: Map<String, List<ErrorFieldDto>>? = null,
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("method")
    val method: String? = null
)
