package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.LanguageApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.language.LanguageRequest
import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.domain.repository.LanguageRepository
import retrofit2.Retrofit
import javax.inject.Inject

/** Gọi API auth be-nest-hrm: POST /auth/login (base URL từ Constants.BASE_URL). */
class LanguageRepositoryImpl @Inject constructor(
    private val apiService: LanguageApiService,
    private val retrofit: Retrofit
) : LanguageRepository {

    override suspend fun language(): NetworkResult<List<LanguageResponse>> =
        safeApiCall(retrofit) {
            apiService.language(LanguageRequest().toMap())
        }
}
