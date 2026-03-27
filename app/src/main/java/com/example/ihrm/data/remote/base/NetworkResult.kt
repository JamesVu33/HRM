package com.example.ihrm.data.remote.base

import com.example.ihrm.core.errorHandler.CommonErrorException

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure(val error: CommonErrorException) : NetworkResult<Nothing>()
    data class Exception(val e: CommonErrorException) : NetworkResult<Nothing>()

    fun getOrNull(): T? = if (this is Success) data else null

    fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(error)
            is Exception -> Exception(e)
        }
    }
}