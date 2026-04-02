package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.dto.SecurityCheckSubmissionDto
import com.example.ihrm.data.remote.securities.SecurityGroupsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SecurityCheckApiService {

    /** GET /security-check/submissions — danh sách bài nộp security check (có meta phân trang). */
    @GET("security-check/submissions")
    suspend fun getSubmissions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<ApiSuccessResponse<List<SecurityCheckSubmissionDto>>>

    @GET("/security-check/groups")
    suspend fun getGroups(): Response<ApiSuccessResponse<List<SecurityGroupsResponse>>>
}
