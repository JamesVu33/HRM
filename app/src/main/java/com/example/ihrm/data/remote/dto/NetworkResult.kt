package com.example.ihrm.data.remote.dto

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T, val message: String) : NetworkResult<T>()
    data class ApiError(val error: AppErrorResponseDto) : NetworkResult<Nothing>()
    data class Exception(val e: Throwable) : NetworkResult<Nothing>()

    fun getOrNull(): T? = if (this is Success) data else null
    
    fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data), message)
            is ApiError -> ApiError(error)
            is Exception -> Exception(e)
        }
    }
}
