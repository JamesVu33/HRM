package com.example.ihrm.domain.model.errors

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.base.ErrorFieldResponse

/**
 * currently API has just support return the first error field
 * so, the errorMsg will contain the key of label and the value of error field only
 *
 * errorType:
 *             "DOMAIN_ERROR" -> "Field value is invalid"
 *             "VALIDATION_ERROR" -> "Invalid input"
 *             "GUARD_ERROR" -> "Access denied"
 *             "HTTP_ERROR" -> "Request failed"
 *             "DB_KNOWN_ERROR", "DB_UNKNOWN_ERROR" -> "Server error"
 *             "INTERNAL_ERROR" -> "Server error"
 *             else -> "Unknown"
 */
data class APIErrorInfo(
    val errorType: String, // _global, DOMAIN_ERROR, VALIDATION_ERROR, GUARD_ERROR, HTTP_ERROR, DB_KNOWN_ERROR, DB_UNKNOWN_ERROR, INTERNAL_ERROR
    val errorKey: String, // label of error field
    val errorMsg: List<ErrorFieldResponse>,
) {
    /**
     * just response the error type,
     * the error message will be gained by local DB or translate API endpoint
     */
    fun getCommonErrorType(): CommonErrorException {
        return when (errorType) {
            "VALIDATION_ERROR",
            "DOMAIN_ERROR" -> {
                CommonErrorException.InvalidInputException(
                    errorField = errorKey, // base on request data class to detect which field is invalid
                    errorMsg = "error",
                    errorKey = errorMsg.firstOrNull()?.key ?: "Unknown",
                    errorParams = errorMsg.firstOrNull()?.params
                )
            }
            "_global" -> CommonErrorException.InvalidLogicException(
                errorMsg = "error",
                errorKey = errorMsg.firstOrNull()?.key ?: "Unknown"
            )
            else -> CommonErrorException.UnknownException("")

        }
    }
}