package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.language.LanguageResponse
import kotlinx.coroutines.flow.Flow

/**
 * Splash operations (language).
 */
interface LanguageRepository {

    suspend fun language(): NetworkResult<List<LanguageResponse>>

    suspend fun saveLanguages(languages: List<LanguageResponse>)

    fun getLanguages(): Flow<List<LanguageResponse>>
}
