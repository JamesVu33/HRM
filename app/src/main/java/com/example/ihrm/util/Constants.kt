package com.example.ihrm.util

object Constants {
    const val AUTH_PREFS_NAME = "auth_prefs"
    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"
    const val PREF_IS_LOGGED_IN = "is_logged_in"
    const val PREF_USER_FULL_NAME = "user_full_name"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_USER_EMPLOYEE_ID = "user_employee_id"
    const val PREF_USER_PHONE = "user_phone"
    const val PREF_ACCOUNT_TYPE = "account_type"
    const val PREF_MODIFIABLE_FEATURES = "modifiable_features"

    const val DEFAULT_PAGE = 1

    const val DEFAULT_LIMIT = 100

    /** Page size cho GET employees list + load more trên EmployeeListScreen. */
    const val EMPLOYEE_LIST_PAGE_LIMIT = 20

    /** Debounce ô tìm kiếm nhân viên trước khi gọi API (tránh giật khi gõ). */
    const val EMPLOYEE_LIST_SEARCH_DEBOUNCE_MS = 400L
    const val DASH = "-"
}