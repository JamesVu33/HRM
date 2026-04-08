package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.organizationresponse.OrganizationResponse
import retrofit2.Response
import retrofit2.http.GET

interface OrganizationApiService {
    @GET("groups")
    suspend fun getOrganizations(): Response<ApiSuccessResponse<List<OrganizationResponse>>>
}