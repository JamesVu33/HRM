package com.example.ihrm.ui.localization

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ihrm.data.remote.language.LanguageResponse

data class LocalizationState(
    val selectedLanguageCode: String,
    val setLanguageCode: (String) -> Unit,
)

val LocalLocalization = staticCompositionLocalOf<LocalizationState> {
    error("LocalLocalization not provided — wrap the tree with ProvideLocalization")
}

/**
 * Subscribes to [AppLanguageController] and exposes [LocalLocalization] for [tr].
 *
 * After the user picks a new language in the dialog, call [android.app.Activity.recreate] on
 * [androidx.activity.ComponentActivity] so [stringResource]/[tr] reload `values-*` (replacing
 * [LocalContext] with [android.content.Context.createConfigurationContext] breaks Material3
 * dynamic color, dialogs, and other APIs that require an [android.app.Activity] context).
 */
@Composable
fun ProvideLocalization(
    controller: AppLanguageController,
    content: @Composable () -> Unit,
) {
    val code by controller.selectedLanguageCode.collectAsStateWithLifecycle()
    val state = remember(code) {
        LocalizationState(
            selectedLanguageCode = code,
            setLanguageCode = controller::setLanguageCode,
        )
    }
    CompositionLocalProvider(LocalLocalization provides state) {
        content()
    }
}

fun pickTranslation(
    row: LanguageResponse?,
    languageCode: String,
    fallback: String,
): String {
    if (row == null) return fallback
    return when (languageCode.lowercase()) {
        "vi" -> row.valueVi.ifBlank { fallback }
        "ko" -> row.valueKr.ifBlank { fallback }
        else -> row.valueEn.ifBlank { fallback }
    }
}

/**
 * UI copy from `res/values`, `res/values-vi`, `res/values-ko` — same as [stringResource].
 * Locale follows [AppLocaleApplier] after [android.app.Activity.recreate] or cold start.
 * Keys missing in `values-vi` / `values-ko` fall back to `values/`.
 */
@Composable
@ReadOnlyComposable
fun tr(@StringRes id: Int): String = stringResource(id)

@Composable
@ReadOnlyComposable
fun tr(@StringRes id: Int, vararg formatArgs: Any): String =
    if (formatArgs.isEmpty()) stringResource(id) else stringResource(id, *formatArgs)
