package com.example.ihrm.util

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.ihrm.data.remote.base.NetworkResult
import java.text.Normalizer
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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

/** `dd/MM/yyyy` (giống [toDisplayDate]) → millis cho Material DatePicker. */
fun String?.parseDisplayDateToPickerMillis(): Long? {
    if (isNullOrBlank()) return null
    return try {
        val parts = trim().split("/")
        if (parts.size != 3) return null
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        LocalDate.of(year, month, day)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    } catch (_: Exception) {
        null
    }
}

/** Millis từ DatePicker → `dd/MM/yyyy` để đồng bộ form My Info / PATCH. */
fun Long.toPickerDisplayDateString(): String =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

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

fun String?.formatDateTime(): String {
    return try {
        val parsed = java.time.OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        parsed.format(formatter)
    } catch (e: Exception) {
        this ?: ("" + e)
    }
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

fun String.getTodayDate(): String {
    val formatter = DateTimeFormatter.ofPattern(
        "EEEE, dd MMM yyyy",
        Locale.getDefault()
    )
    return LocalDate.now().format(formatter)
}

data class SecurityLegendMeta(
    val key: String,
    val label: String,
    val chipText: String,
    val chipTextColor: Color,
    val chipBackground: Color,
    val highlightedQuestionIndices: Set<Int> = emptySet(),
    val outlinedQuestionIndices: Set<Int> = emptySet(),
    val showPendingActions: Boolean = false,
) {
    companion object {
        val DEFAULT = SecurityLegendMeta(
            key = "",
            label = "",
            chipText = "",
            chipTextColor = Color.Unspecified,
            chipBackground = Color.Unspecified,
        )
    }
}

fun legendByKey(key: String): SecurityLegendMeta {
    return when (key.lowercase()) {
        "approved" -> SecurityLegendMeta(
            key = key,
            label = "Approved",
            chipText = "APPROVED",
            chipTextColor = Color(0xFF008236),
            chipBackground = Color(0xFFDCFCE7),
        )

        "rejected" -> SecurityLegendMeta(
            key = key,
            label = "Rejected",
            chipText = "REJECT",
            chipTextColor = Color(0xFFF10C00),
            chipBackground = Color(0xFFFFC1C2),
            highlightedQuestionIndices = setOf(1),
        )

        "submitted" -> SecurityLegendMeta(
            key = key,
            label = "Submitted",
            chipText = "SUBMITTED",
            chipTextColor = Color(0xFF007AFF),
            chipBackground = Color(0xFFDDEBFF),
            showPendingActions = true,
        )

        "pending" -> SecurityLegendMeta(
            key = key,
            label = "Pending",
            chipText = "PENDING",
            chipTextColor = Color(0xFFB35A00),
            chipBackground = Color(0xFFF9D9A7),
            outlinedQuestionIndices = setOf(3),
            showPendingActions = true,
        )

        else -> SecurityLegendMeta(
            key = key,
            label = "Not Submitted",
            chipText = "NOT SUBMITTED",
            chipTextColor = Color(0xFF4B5563),
            chipBackground = Color(0xFFE5E7EB),
        )
    }
}