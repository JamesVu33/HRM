package com.example.ihrm.util

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.base.NetworkResult
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

typealias EmptyFunc = () -> Unit
typealias ParamFunc<T> = (data: T) -> Unit
typealias SupEmptyFunc<T> = suspend () -> T

fun String?.toDisplayDate(): String {
    return try {
        val instant = Instant.parse(this)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    } catch (e: Exception) {
        ""
    }
}

inline fun <A, B, R> combineResult(
    a: NetworkResult<A>,
    b: NetworkResult<B>,
    transform: (A, B) -> R
): NetworkResult<R> {
    a.getErrorOrNull()?.let { return it }
    b.getErrorOrNull()?.let { return it }

    val dataA = (a as NetworkResult.Success).data
    val dataB = (b as NetworkResult.Success).data

    return NetworkResult.Success(transform(dataA, dataB))
}

fun <T> NetworkResult<T>.getErrorOrNull(): NetworkResult<Nothing>? {
    return when (this) {
        is NetworkResult.Failure -> this
        is NetworkResult.Exception -> this
        else -> null
    }
}