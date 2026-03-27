package com.example.ihrm.data.remote.base

import com.example.ihrm.domain.model.errors.APIErrorInfo
import com.google.gson.annotations.SerializedName

/**
 * Error response body from be-nest-hrm (4xx/5xx).
 */
data class AppErrorResponse(
    @SerializedName("errorType")
    val errorType: String,
    @SerializedName("errors")
    val errors: Map<String, List<ErrorFieldResponse>>? = null,
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("method")
    val method: String? = null
) {
    fun getErrorInfo(): APIErrorInfo = APIErrorInfo(
        errorType = errorType,
        errorKey = errors?.keys?.firstOrNull() ?: "",
        errorMsg = errors?.values?.firstOrNull() ?: emptyList(),
    )
}