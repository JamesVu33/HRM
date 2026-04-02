package com.example.ihrm.ui.security.checks

/**
 * Chế độ tìm theo ô search (khớp với nhóm "Search by" trong filter sheet).
 */
enum class SecurityChecksSearchByMode {
    ALL,
    EMPLOYEE_ID,
    NAME,
}

/**
 * Bộ filter đang áp dụng cho danh sách security check submissions (client-side).
 */
data class SecurityChecksActiveFilters(
    val dateFromMillis: Long? = null,
    val dateToMillis: Long? = null,
    val searchBy: SecurityChecksSearchByMode = SecurityChecksSearchByMode.ALL,
    /** `null` = mọi group. Lưu `code` (hoặc `name` nếu không có code); lọc theo `group.id` hoặc `group.code` trên submission. */
    val groupId: String? = null,
) {
    fun hasActiveFilters(): Boolean =
        dateFromMillis != null ||
            dateToMillis != null ||
            searchBy != SecurityChecksSearchByMode.ALL ||
            groupId != null

    companion object {
        val Default = SecurityChecksActiveFilters()
    }
}