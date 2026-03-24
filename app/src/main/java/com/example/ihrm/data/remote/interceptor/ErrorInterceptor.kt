package com.example.ihrm.data.remote.interceptor

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.errorHandler.GlobalErrorHandler
import com.example.ihrm.data.remote.base.AppErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor(
    val gson: Gson
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (!response.isSuccessful) {
            val errorKey = parseApiErrorMessage(response.body?.string())
            when(response.code) {
                401 -> CommonErrorException.UnauthorizedException(errorKey ?: "GLOBAL_ERROR_UNAUTHORIZED") // invalid token
                403 -> CommonErrorException.NotPermissionException(errorKey ?: "GLOBAL_ERROR_FORBIDDEN") // permission not allow
                else -> null
            }?.let { GlobalErrorHandler.showError(it.errorKey) }

        }
        return response
    }

    /**
     * Parses be-nest-hrm error body (errorType, errors) into a user-friendly message.
     */
    private fun parseApiErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val dto = gson.fromJson(errorBody, AppErrorResponse::class.java)
            dto?.errors?.get("_global")?.firstOrNull()?.key
        } catch (_: JsonSyntaxException) {
            null
        }
    }
}


