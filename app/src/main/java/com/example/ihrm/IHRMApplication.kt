package com.example.ihrm

import android.app.Application
import com.example.ihrm.ui.localization.AppLocaleApplier
import com.example.ihrm.ui.localization.AppLanguagePreferences
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IHRMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AuthManager.init(this)
        // Mỗi lần process mới (cold start, user swipe kill app): không khôi phục phiên từ disk — bắt buộc đăng nhập lại.
        AuthManager.clearTokens()
        applyStoredAppLocale()
    }

    /** Same prefs as [AppLanguagePreferences], so the first [Activity] uses the correct `values-*`. */
    private fun applyStoredAppLocale() {
        val prefs = getSharedPreferences(AppLanguagePreferences.PREFS_NAME, MODE_PRIVATE)
        val code =
            prefs.getString(AppLanguagePreferences.KEY_LANGUAGE_CODE, AppLanguagePreferences.DEFAULT_CODE)
                ?: AppLanguagePreferences.DEFAULT_CODE
        AppLocaleApplier.apply(code)
    }
}