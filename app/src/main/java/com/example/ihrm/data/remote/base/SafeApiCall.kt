package com.example.ihrm.data.remote.base

import com.example.ihrm.data.remote.dto.ApiResponseDto
import com.example.ihrm.data.remote.dto.AppErrorResponseDto
import com.example.ihrm.data.remote.dto.NetworkResult
import com.example.ihrm.data.remote.dto.ResponseListDto
import com.example.ihrm.domain.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit

suspend fun <T> safeApiCall(
    retrofit: Retrofit, // Needed to get the correct JSON converter
    execute: suspend () -> Response<ApiResponseDto<T>>
): NetworkResult<T> = withContext(Dispatchers.IO) {
    return@withContext try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            // SUCCESS CASE: Extract the generic data 'T'
            // We return body.data because that's the actual object the UI needs
            // Note: If body.data is null but it's Success, we might need to handle it or use a default
            NetworkResult.Success<T>(body.data as T, body.message)
        } else {
            // ERROR CASE: Manually parse the errorBody
            val errorBody = response.errorBody()
            val adapter = retrofit.responseBodyConverter<AppErrorResponseDto>(
                AppErrorResponseDto::class.java,
                emptyArray()
            )
            val errorResponse = errorBody?.let { adapter.convert(it) }
                ?: AppErrorResponseDto(errorType = "UNKNOWN_ERROR")

            NetworkResult.ApiError(errorResponse)
        }
    } catch (e: Exception) {
        NetworkResult.Exception(e)
    }
}
