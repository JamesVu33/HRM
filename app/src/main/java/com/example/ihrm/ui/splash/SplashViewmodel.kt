package com.example.ihrm.ui.splash

import androidx.compose.runtime.Immutable
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.domain.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@Immutable
data class SplashUiState(
    val isLoadingCompleted: Boolean = false,
)

@HiltViewModel
class SplashViewmodel @Inject constructor(
val languageRepository: LanguageRepository,
) : BaseViewmodel() {
    val uiState = MutableStateFlow(SplashUiState())

    init {
        language()
    }

    private fun language() {
        fetchData(
            fetching = { languageRepository.language() },
            callbackWrapper = object : CallbackWrapper<List<LanguageResponse>> {
                override fun onSuccess(data: List<LanguageResponse>) {
                    uiState.value = uiState.value.copy(isLoadingCompleted = true)
                }

                override suspend fun doOnBackground(data: List<LanguageResponse>) {
                    // save into local db
                    languageRepository.saveLanguages(data)
                }
            }
        )
    }

}