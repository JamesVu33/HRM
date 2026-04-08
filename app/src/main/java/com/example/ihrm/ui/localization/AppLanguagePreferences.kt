package com.example.ihrm.ui.localization

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists the user's UI language choice (en / vi / ko).
 * Keys must match [ChangeLanguageDialog] codes.
 */
@Singleton
class AppLanguagePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLanguageCode(): String =
        prefs.getString(KEY_LANGUAGE_CODE, DEFAULT_CODE) ?: DEFAULT_CODE

    fun setLanguageCode(code: String) {
        prefs.edit().putString(KEY_LANGUAGE_CODE, code.lowercase()).apply()
    }

    companion object {
        /** Shared with [com.example.ihrm.IHRMApplication] to apply locale before first frame. */
        const val PREFS_NAME = "ihrm_app_settings"
        const val KEY_LANGUAGE_CODE = "app_language_code"
        const val DEFAULT_CODE = "en"
    }
}
