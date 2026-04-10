package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.data.remote.securities.SecurityCheckStatusResponse

interface MySecurityCheckRepository {
    suspend fun getMySecurityCheck(
        year: Int?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
    ): NetworkResult<List<MySecurityCheckResponse>>

    suspend fun getHasSubmitted(): NetworkResult<SecurityCheckStatusResponse>
}