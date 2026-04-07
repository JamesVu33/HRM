package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.organizationresponse.OrganizationResponse

interface OrganizationRepository {
    suspend fun getOrganizations(): NetworkResult<List<OrganizationResponse>>
}