package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.OrganizationApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.organizationresponse.OrganizationResponse
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.repository.OrganizationRepository
import retrofit2.Retrofit
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val organizationApiService: OrganizationApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit
): OrganizationRepository {
    override suspend fun getOrganizations(): NetworkResult<List<OrganizationResponse>> =
        safeApiCall(retrofit) {
            organizationApiService.getOrganizations()
        }
}