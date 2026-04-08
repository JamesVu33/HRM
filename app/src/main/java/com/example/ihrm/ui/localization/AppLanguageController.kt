package com.example.ihrm.ui.localization

import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.domain.repository.LanguageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds translation rows from Room (synced from `/translations`) and the selected app language.
 */
@Singleton
class AppLanguageController @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val appLanguagePreferences: AppLanguagePreferences,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _translations =
        MutableStateFlow<Map<String, LanguageResponse>>(emptyMap())
    val translations: StateFlow<Map<String, LanguageResponse>> = _translations.asStateFlow()

    private val _selectedLanguageCode =
        MutableStateFlow(appLanguagePreferences.getLanguageCode())
    val selectedLanguageCode: StateFlow<String> = _selectedLanguageCode.asStateFlow()

    init {
        scope.launch {
            languageRepository.getLanguages().collect { list ->
                _translations.value = list.associateBy { it.key }
            }
        }
    }

    fun setLanguageCode(code: String) {
        val normalized = code.lowercase()
        appLanguagePreferences.setLanguageCode(normalized)
        _selectedLanguageCode.value = normalized
        AppLocaleApplier.apply(normalized)
    }
}
