package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.SecurityCheckApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.model.securitycheck.SecurityCheckDetail
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate
import com.example.ihrm.domain.repository.SecurityCheckDetailRepository
import retrofit2.Retrofit
import javax.inject.Inject

class SecurityCheckDetailRepositoryImpl @Inject constructor(
    private val apiService: SecurityCheckApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit,
) : SecurityCheckDetailRepository {
    override suspend fun getSecurityCheckDetail(id: String): NetworkResult<SecurityCheckDetail> =
        safeApiCall(retrofit) {
            apiService.getSubmissionDetails(id)
        }.map { it.fromResponseToInfo() }

    override suspend fun getSecurityTemplates(id: Int): NetworkResult<SecurityTemplate> =
        safeApiCall(retrofit) {
            apiService.getSecurityTemplates(id)
        }.map { it.fromResponseToInfo() }
}