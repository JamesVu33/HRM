package com.example.ihrm.ui.localization

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Maps app language codes (dialog / prefs) to resource qualifiers `values`, `values-vi`, `values-ko`.
 *
 * [wrapContextWithStoredLocale] is applied in [com.example.ihrm.MainActivity.attachBaseContext] so
 * [androidx.compose.ui.res.stringResource] / [tr] resolve the correct `values-*` even when using
 * [androidx.activity.ComponentActivity] (where [AppCompatDelegate.setApplicationLocales] alone is not
 * always enough).
 */
object AppLocaleApplier {

    private fun languageTagForCode(languageCode: String): String =
        when (languageCode.lowercase()) {
            "vi" -> "vi"
            "ko" -> "ko"
            else -> "en"
        }

    fun apply(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageTagForCode(languageCode))
        )
    }

    /**
     * Reads [AppLanguagePreferences] and returns a context whose [Configuration] matches the
     * stored language (for [android.app.Activity.attachBaseContext]).
     */
    fun wrapContextWithStoredLocale(base: Context): Context {
        val prefs = base.getSharedPreferences(AppLanguagePreferences.PREFS_NAME, Context.MODE_PRIVATE)
        val code =
            prefs.getString(AppLanguagePreferences.KEY_LANGUAGE_CODE, AppLanguagePreferences.DEFAULT_CODE)
                ?: AppLanguagePreferences.DEFAULT_CODE
        return wrapContextWithLanguageCode(base, code)
    }

    fun wrapContextWithLanguageCode(base: Context, languageCode: String): Context {
        val tag = languageTagForCode(languageCode)
        val config = Configuration(base.resources.configuration)
        config.setLocales(LocaleList.forLanguageTags(tag))
        return base.createConfigurationContext(config)
    }
}
