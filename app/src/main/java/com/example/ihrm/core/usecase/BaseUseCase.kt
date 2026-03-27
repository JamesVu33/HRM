package com.example.ihrm.core.usecase

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.firstOrNull

abstract class BaseUseCase {
    abstract var languageRepository: LanguageRepository

    // todo: listen the current language selected and then get corresponding language from db

    suspend fun <T> translateResponse(result: NetworkResult<T>): NetworkResult<T> {
        if (result is NetworkResult.Failure) {
            val language = languageRepository.getLanguageByKey(result.error.errorKey).firstOrNull()
            var errorMsgResult = ""
            language?.let {
                Log.d("APIFlows", "translateResponse: got $it")
                errorMsgResult = it.valueEn
            }

            if (result.error is CommonErrorException.InvalidInputException && !result.error.errorParams.isNullOrEmpty()) {
                result.error.errorParams.forEach { (key, value) ->
                    val temp = errorMsgResult
                    if (temp.contains("{{$key}}") && value is String) {
                        errorMsgResult = temp.replace("{{$key}}", value, true)
                    }
                }
            }
            result.error.errorMsg = errorMsgResult

            return result
        } else {
            return result
        }
    }
}
