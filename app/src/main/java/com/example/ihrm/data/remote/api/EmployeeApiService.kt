package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.dto.LevelResponseDto
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmployeeApiService {
    /** GET /employees - returns wrapped list of users (be-nest-hrm). Requires auth. */
    @GET("employees")
    suspend fun getAllEmployees(): Response<ApiSuccessResponse<List<UserResponseDto>>>

    /** GET /employees/meta - roles, titles, levels for mapping. Use when user level is S1/S2. */
    @GET("employees/meta")
    suspend fun getEmployeesMeta(): Response<ApiSuccessResponse<UserMetaResponseDto>>

    /** GET /me/employee-info - current user's level, title, roles. */
    @GET("me/employee-info")
    suspend fun getMeEmployeeInfo(): Response<ApiSuccessResponse<MeEmployeeResponse>>

    /** GET /levels - all levels for mapping levelId -> level (code/name) on dashboard. */
    @GET("levels")
    suspend fun getAllLevels(): Response<ApiSuccessResponse<List<LevelResponseDto>>>

    /** GET /levels/{id} - level by id. */
    @GET("levels/{id}")
    suspend fun getLevelById(@Path("id") id: Int): Response<ApiSuccessResponse<LevelResponseDto>>

    /** GET /employees/{id} - response: { statusCode, message, data: { ..., level?: { id, code, name, ... } } }. */
    @GET("employees/{id}")
    suspend fun getEmployeeById(@Path("id") id: String): Response<ApiSuccessResponse<EmployeeDto>>

    @POST("employees")
    suspend fun createEmployee(@Body employee: EmployeeDto): Response<ApiSuccessResponse<EmployeeDto>>

    @PUT("employees/{id}")
    suspend fun updateEmployee(
        @Path("id") id: String,
        @Body employee: EmployeeDto
    ): Response<ApiSuccessResponse<EmployeeDto>>

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: String): Response<ApiSuccessResponse<Unit>>
}
