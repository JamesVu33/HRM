package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups

interface SecurityCheckRepository {

    suspend fun getSubmissions(
        page: Int = 1,
        limit: Int = 100,
    ): NetworkResult<SecurityCheckSubmissionsPage>

    suspend fun getGroups(): NetworkResult<List<SecurityGroups>>
}
