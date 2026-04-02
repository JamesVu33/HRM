package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups

interface SecurityCheckRepository {

    suspend fun getSubmissions(
        fromDate: String?,
        toDate: String?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
        type: String?,
        monthCode: String?,
        groupId: String?,
    ): NetworkResult<SecurityCheckSubmissionsPage>

    suspend fun getGroups(): NetworkResult<List<SecurityGroups>>
}
