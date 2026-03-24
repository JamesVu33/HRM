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

/**
 * Base package cho gọi API thống nhất: chạy trên IO, xử lý 401, map sang Result.
 * Mỗi lần call API dùng [safeApiCall] hoặc [safeApiCallList] để có xử lý lỗi nhất quán.
 */

/**
 * Gọi API trả về [ApiResponseDto] (response dạng { statusCode, message, data }).
 * - 401 → [UnauthorizedException]
 * - 2xx → [Result.success] với data (có thể null)
 * - Khác → [Result.failure] với message từ response hoặc exception.
 */
suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> ApiResponseDto<T>
): Result<T?> = withContext(Dispatchers.IO) {
    try {
        val response = block()
        when {
            response.statusCode == 401 -> Result.failure(UnauthorizedException())
            response.statusCode in 200..299 -> Result.success(response.data)
            else -> Result.failure(Exception(response.message))
        }
    } catch (e: HttpException) {
        if (e.code() == 401) Result.failure(UnauthorizedException()) else Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * Gọi API trả về [ResponseListDto] (response dạng { statusCode, message, data: List }).
 * - 401 → [UnauthorizedException]
 * - 2xx → [Result.success] với list data
 * - Khác → [Result.failure].
 */
suspend inline fun <T> safeApiCallList(
    crossinline block: suspend () -> ResponseListDto<T>
): Result<List<T>> = withContext(Dispatchers.IO) {
    try {
        val response = block()
        when {
            response.statusCode == 401 -> Result.failure(UnauthorizedException())
            response.statusCode in 200..299 -> Result.success(response.data)
            else -> Result.failure(Exception(response.message ?: "Request failed"))
        }
    } catch (e: HttpException) {
        if (e.code() == 401) Result.failure(UnauthorizedException()) else Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

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
