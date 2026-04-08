package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse

interface MySecurityCheckRepository {
    suspend fun getMySecurityCheck(
        year: Int?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
    ): NetworkResult<List<MySecurityCheckResponse>>
}