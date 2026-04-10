package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.SecurityCheckApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.data.remote.securities.SecurityCheckStatusResponse
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.repository.MySecurityCheckRepository
import retrofit2.Retrofit
import javax.inject.Inject

class MySecurityRepositoryImpl @Inject constructor(
    private val apiService: SecurityCheckApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit
) : MySecurityCheckRepository {
    override suspend fun getMySecurityCheck(
        year: Int?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?
    ): NetworkResult<List<MySecurityCheckResponse>> =
        safeApiCall(retrofit) {
            apiService.getMySecurityCheckSubmissions(
                year = year,
                query = query?.takeIf { it.isNotBlank() },
                page = page,
                limit = limit,
                orderBy = orderBy,
                sortBy = sortBy,
                status = status
            )
        }

    override suspend fun getHasSubmitted(): NetworkResult<SecurityCheckStatusResponse> =
        safeApiCall(retrofit) {
            apiService.getHasSubmitted()
        }
}