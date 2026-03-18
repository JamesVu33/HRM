package com.example.ihrm.util

object Constants {
    /** Backend base URL. Use be-nest-hrm or be-nest-hem depending on your Render service name. */
    const val BASE_URL = "https://be-nest-hrm.onrender.com/"
    const val DATABASE_NAME = "employee_database"
    const val AUTH_PREFS_NAME = "auth_prefs"
    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"
    const val PREF_IS_LOGGED_IN = "is_logged_in"
    const val PREF_USER_FULL_NAME = "user_full_name"
    const val PREF_USER_EMAIL = "user_email"
}