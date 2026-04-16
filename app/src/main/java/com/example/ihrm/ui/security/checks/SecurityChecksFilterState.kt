package com.example.ihrm.ui.security.checks

import java.time.LocalDate
import java.time.ZoneId

/**
 * Chế độ tìm theo ô search (khớp với nhóm "Search by" trong filter sheet).
 */
enum class SecurityChecksSearchByMode {
    ALL,
    EMPLOYEE_ID,
    NAME,
}

/**
 * Bộ filter đang áp dụng cho security checks (đồng bộ API submissions / stats / missing).
 */
data class SecurityChecksActiveFilters(
    val dateFromMillis: Long? = null,
    val dateToMillis: Long? = null,
    val searchBy: SecurityChecksSearchByMode = SecurityChecksSearchByMode.ALL,
    /** `null` = mọi group. Ưu tiên `id` từ API; fallback `code` / `name`. */
    val groupId: String? = null,
) {
    /** So với baseline (đầu tháng → hôm nay, ALL, mọi group): có khác thì hiện chấm filter. */
    fun hasActiveFiltersComparedTo(baseline: SecurityChecksActiveFilters): Boolean = this != baseline

    companion object {
        val Default = SecurityChecksActiveFilters()

        /** Từ đầu tháng đến **hôm nay** (múi giờ máy), search by ALL, mọi group — mặc định màn Security Checks. */
        fun defaultCurrentMonth(): SecurityChecksActiveFilters {
            val zone = ZoneId.systemDefault()
            val today = LocalDate.now(zone)
            val start = today.withDayOfMonth(1)
            val fromMillis = start.atStartOfDay(zone).toInstant().toEpochMilli()
            val toMillis = today.atTime(23, 59, 59, 999_999_999).atZone(zone).toInstant().toEpochMilli()
            return SecurityChecksActiveFilters(
                dateFromMillis = fromMillis,
                dateToMillis = toMillis,
                searchBy = SecurityChecksSearchByMode.ALL,
                groupId = null,
            )
        }
    }
}