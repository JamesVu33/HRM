package com.example.ihrm.ui.common

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

/**
 * Giữ state debounce qua recomposition (khác với gọi [com.example.ihrm.util.singleClick] trực tiếp trong body composable).
 */
@Composable
fun rememberThrottledClick(
    intervalMs: Long = 600L,
    onClick: () -> Unit,
): () -> Unit {
    val latest by rememberUpdatedState(onClick)
    return remember(intervalMs) {
        var lastClickTime = 0L
        {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime >= intervalMs) {
                lastClickTime = now
                latest()
            }
        }
    }
}
