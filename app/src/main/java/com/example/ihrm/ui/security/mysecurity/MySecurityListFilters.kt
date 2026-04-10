package com.example.ihrm.ui.security.mysecurity

enum class MySecurityListStatusFilter {
    ALL,
    SUBMITTED,
    REJECTED,
    APPROVED;

    fun toApiStatusParam(): String? = when (this) {
        ALL -> null
        SUBMITTED -> "submitted"
        REJECTED -> "rejected"
        APPROVED -> "approved"
    }
}

data class MySecurityListFilters(
    val status: MySecurityListStatusFilter = MySecurityListStatusFilter.ALL,
    val year: Int? = null,
) {
    fun hasActiveFilters(): Boolean =
        status != MySecurityListStatusFilter.ALL || year != null

    companion object {
        val Default = MySecurityListFilters()
    }
}
