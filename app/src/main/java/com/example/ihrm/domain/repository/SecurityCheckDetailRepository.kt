package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.securitycheck.SecurityCheckDetail
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate

interface SecurityCheckDetailRepository {
    suspend fun getSecurityCheckDetail(id: String): NetworkResult<SecurityCheckDetail>
    suspend fun getSecurityTemplates(id: Int): NetworkResult<SecurityTemplate>
}