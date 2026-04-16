package com.example.ihrm.ui.security.checks

import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Tham số query dùng chung cho GET submissions, missing-submissions và submissions/stats.
 *
 * BE (Nest) validate `fromDate` / `toDate` là **UTC date-time** (ISO-8601 instant), không chấp nhận chỉ `yyyy-MM-dd`.
 */
data class SecurityChecksApiQuery(
    val fromDate: String?,
    val toDate: String?,
    val query: String?,
    val type: String?,
    val monthCode: String?,
    val groupId: String?,
)

private val isoInstantUtc: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

fun SecurityChecksActiveFilters.toApiQuery(searchText: String): SecurityChecksApiQuery {
    val fromDate = dateFromMillis?.let { millis ->
        isoInstantUtc.format(Instant.ofEpochMilli(millis))
    }
    val toDate = dateToMillis?.let { millis ->
        isoInstantUtc.format(Instant.ofEpochMilli(millis))
    }
    val query = searchText.trim().takeIf { it.isNotBlank() }
    val type = when (searchBy) {
        SecurityChecksSearchByMode.ALL -> null
        SecurityChecksSearchByMode.EMPLOYEE_ID -> "EMPLOYEE_ID"
        SecurityChecksSearchByMode.NAME -> "NAME"
    }
    return SecurityChecksApiQuery(
        fromDate = fromDate,
        toDate = toDate,
        query = query,
        type = type,
        monthCode = null,
        groupId = groupId?.takeIf { it.isNotBlank() },
    )
}
