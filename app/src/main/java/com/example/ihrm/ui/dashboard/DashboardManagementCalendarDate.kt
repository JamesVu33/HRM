package com.example.ihrm.ui.dashboard

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Ngày hiển thị trên thẻ calendar tab Management (theo languageCode app: vi / en / ko). */
internal data class ManagementCalendarDateLabels(
    val dayOfMonth: String,
    val monthLabel: String,
    val weekdayLabel: String,
)

internal fun localeForAppLanguageCode(languageCode: String): Locale =
    when (languageCode.lowercase()) {
        "vi" -> Locale("vi", "VN")
        "ko" -> Locale("ko", "KR")
        else -> Locale.US
    }

/**
 * Định dạng một [LocalDate] cho header calendar (số ngày + tháng rút gọn + thứ rút gọn).
 * Locale khớp ba ngôn ngữ trong app (vi / en / ko).
 */
internal fun formatManagementCalendarDate(
    date: LocalDate,
    languageCode: String,
): ManagementCalendarDateLabels {
    val locale = localeForAppLanguageCode(languageCode)
    val dayFmt = DateTimeFormatter.ofPattern("d", locale)
    val monthFmt = DateTimeFormatter.ofPattern("MMM", locale)
    val weekdayFmt = DateTimeFormatter.ofPattern("EEE", locale)
    return ManagementCalendarDateLabels(
        dayOfMonth = date.format(dayFmt),
        monthLabel = date.format(monthFmt).trim(),
        weekdayLabel = date.format(weekdayFmt).trim(),
    )
}

internal fun formatManagementCalendarToday(languageCode: String): ManagementCalendarDateLabels =
    formatManagementCalendarDate(LocalDate.now(), languageCode)
