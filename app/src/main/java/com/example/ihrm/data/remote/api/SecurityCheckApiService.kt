package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.dto.ChecksSecuritySubmissionResponse
import com.example.ihrm.data.remote.employee.EmployeeResponse
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.data.remote.securities.SecurityCheckDashboardResponse
import com.example.ihrm.data.remote.securities.SecurityCheckDetailResponse
import com.example.ihrm.data.remote.securities.SecurityCheckStatusResponse
import com.example.ihrm.data.remote.securities.SecurityGroupsResponse
import com.example.ihrm.data.remote.securities.SecuritySubmissionRequest
import com.example.ihrm.data.remote.securities.SecuritySubmissionResponse
import com.example.ihrm.data.remote.securities.SecurityTemplateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SecurityCheckApiService {

    /** GET /security-check/submissions — danh sách bài nộp security check (có meta phân trang). */
    @GET("security-check/submissions")
    suspend fun getSubmissions(
        @Query("fromDate") fromDate: String? = null,
        @Query("toDate") toDate: String? = null,
        @Query("query") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("monthCode") monthCode: String? = null,
        @Query("groupId") groupId: String? = null,
    ): Response<ApiSuccessResponse<ChecksSecuritySubmissionResponse>>

    @GET("employees/missing-submissions")
    suspend fun getNotSubmissions(
        @Query("fromDate") fromDate: String? = null,
        @Query("toDate") toDate: String? = null,
        @Query("query") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("monthCode") monthCode: String? = null,
        @Query("groupId") groupId: String? = null,
    ): Response<ApiSuccessResponse<List<EmployeeResponse>>>

    @GET("/security-check/groups")
    suspend fun getGroups(): Response<ApiSuccessResponse<List<SecurityGroupsResponse>>>

    @GET("/security-check/submissions/{id}")
    suspend fun getSubmissionDetails(
        @Path("id") id: String,
    ): Response<ApiSuccessResponse<SecurityCheckDetailResponse>>

    @GET("/security-check/templates/{id}")
    suspend fun getSecurityTemplates(
        @Path("id") id: Int,
    ): Response<ApiSuccessResponse<SecurityTemplateResponse>>

    @GET("/security-check/submissions/self")
    suspend fun getMySecurityCheckSubmissions(
        @Query("year") year: Int? = null,
        @Query("query") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("status") status: String? = null,
    ): Response<ApiSuccessResponse<List<MySecurityCheckResponse>>>

    @GET("/dashboard/security-check")
    suspend fun getDashboardSecurityCheck(): Response<ApiSuccessResponse<SecurityCheckDashboardResponse>>

    @GET("/security-check/templates/current")
    suspend fun getCurrentSecurityTemplate(): Response<ApiSuccessResponse<SecurityTemplateResponse>>

    @POST("security-check/submissions")
    suspend fun postSubmission(
        @Body request: SecuritySubmissionRequest
    ): Response<ApiSuccessResponse<SecuritySubmissionResponse>>

    @GET("/security-check/submissions/has-submitted")
    suspend fun getHasSubmitted(): Response<ApiSuccessResponse<SecurityCheckStatusResponse>>
}
