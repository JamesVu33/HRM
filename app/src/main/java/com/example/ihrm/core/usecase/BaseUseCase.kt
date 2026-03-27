package com.example.ihrm.core.usecase

import android.util.Log
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.firstOrNull

abstract class BaseUseCase {
    abstract var languageRepository: LanguageRepository
    // todo: listen the current language selected and then get corresponding language from db

    suspend fun <T> translateResponse(result: NetworkResult<T>): NetworkResult<T> {
        if (result is NetworkResult.Failure) {
            val language = languageRepository.getLanguageByKey(result.error.errorKey).firstOrNull()
            language?.let {
                Log.d("APIFlows", "translateResponse: got $it")
                result.error.errorMsg = it.valueEn
            }
            return result
        } else {
            return result
        }
    }
}
