package com.example.ihrm.util

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