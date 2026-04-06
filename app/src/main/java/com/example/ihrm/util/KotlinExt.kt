package com.example.ihrm.util

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ihrm.data.remote.base.NetworkResult
import java.text.Normalizer
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

@Composable
fun (() -> Unit).singleClick(intervalMs: Long = 600L): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime >= intervalMs) {
            lastClickTime = now
            this()
        }
    }
}

fun String.removeVietnameseAccents(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .replace("đ", "d")
        .replace("Đ", "D")
}

fun String.formatVNPhoneNumber(): String {
    val digits = this.filter { it.isDigit() }

    return when {
        // Trường hợp bắt đầu bằng 0 và có 10 chữ số (0918122223)
        digits.length == 10 && digits.startsWith("0") -> {
            val areaCode = digits.substring(1, 4) // 918
            val mid = digits.substring(4, 7)      // 122
            val end = digits.substring(7)         // 2223
            "+84($areaCode) $mid-$end"
        }
        // Trường hợp đã có 84 ở đầu (84918122223)
        digits.length == 11 && digits.startsWith("84") -> {
            val areaCode = digits.substring(2, 5)
            val mid = digits.substring(5, 8)
            val end = digits.substring(8)
            "+84($areaCode) $mid-$end"
        }
        else -> this
    }
}