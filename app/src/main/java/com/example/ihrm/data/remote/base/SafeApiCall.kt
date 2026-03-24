package com.example.ihrm.data.remote.base

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.base.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

suspend fun <T> safeApiCall(
    retrofit: Retrofit, // Needed to get the correct JSON converter
    execute: suspend () -> Response<ApiSuccessResponse<T>>
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
            val adapter = retrofit.responseBodyConverter<AppErrorResponse>(
                AppErrorResponse::class.java,
                emptyArray()
            )
            val errorResponse = errorBody?.let { adapter.convert(it) }
                ?: AppErrorResponse(errorType = "UNKNOWN_ERROR")

            val errorInfo = errorResponse.getErrorInfo()
            val errorType = errorInfo.getCommonErrorType()

            // TODO: get errorMsg from localDB

            NetworkResult.Failure(errorType)
        }
    } catch (throwable: Exception) {
        val errorException = when (throwable) {
            is JSONException, is SocketTimeoutException, is SSLException, is ConnectException, is UnknownHostException -> {
                CommonErrorException.NetworkException(
                    throwable.message ?: "An unexpected error occurred"
                )
            }

            is IOException -> CommonErrorException.NetworkException(
                throwable.message ?: "An unexpected error occurred"
            )

            is HttpException -> CommonErrorException.ServerException(
                throwable.message ?: "Server exception"
            )

            else -> CommonErrorException.UnknownException(
                throwable.message ?: "An unexpected error occurred"
            )
        }
        NetworkResult.Exception(errorException)
    }
}
