package com.example.ihrm.data.remote.base

import android.util.Log
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

        Log.i("apiFlows", "safeApiCall: - start")
        if (response.isSuccessful && body != null) {
            Log.i("apiFlows", "safeApiCall: - success")
            // SUCCESS CASE: Extract the generic data 'T'
            // We return body.data because that's the actual object the UI needs
            // Note: If body.data is null but it's Success, we might need to handle it or use a default
            NetworkResult.Success<T>(body.data as T)
        } else {
            Log.i("apiFlows", "safeApiCall: - failure")
            // ERROR CASE: Manually parse the errorBody
            val errorBody = response.errorBody()
            val adapter = retrofit.responseBodyConverter<AppErrorResponse>(
                AppErrorResponse::class.java,
                emptyArray()
            )
            val errorResponse = errorBody?.let { adapter.convert(it) }
                ?: AppErrorResponse(errorType = "UNKNOWN_ERROR")

            Log.d("apiFlows", "safeApiCall: - failure case to class: $errorResponse")
            val errorInfo = errorResponse.getErrorInfo()
            Log.d("apiFlows", "safeApiCall: - failure case to errorInfo: $errorInfo")
            val errorType = errorInfo.getCommonErrorType()
            Log.d("apiFlows", "safeApiCall: - failure case to errorType: $errorType")

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
        Log.e("apiFlows", "safeApiCall: - exception: $errorException")
        NetworkResult.Exception(errorException)
    }
}

suspend fun <T> safeApiCallRaw(
    execute: suspend () -> Response<T>
): NetworkResult<T> = withContext(Dispatchers.IO) {
    return@withContext try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Exception(
                CommonErrorException.ServerException(
                    "HTTP ${response.code()} ${response.message()}"
                )
            )
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
        Log.e("apiFlows", "safeApiCallRaw: - exception: $errorException")
        NetworkResult.Exception(errorException)
    }
}
