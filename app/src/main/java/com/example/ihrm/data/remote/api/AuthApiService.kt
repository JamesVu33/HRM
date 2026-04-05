package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.login.LoginRequest
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.login.PermissionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Auth API từ be-nest-hrm.
 * Swagger: https://be-nest-hrm.onrender.com/swagger#/Auth/AuthController_login
 */
interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiSuccessResponse<LoginResponse>>

    @GET("/permissions/roles/{id}")
    suspend fun getPermission(@Path("id") id: Int): Response<ApiSuccessResponse<PermissionResponse>>

    @GET("/me/permissions")
    suspend fun getPermission2(): Response<ApiSuccessResponse<List<PermissionResponse>>>

    /**
     * POST /auth/refresh-token
     * Một số Backend NestJS yêu cầu Body rỗng {} để tránh lỗi 400.
     */
    @POST("auth/refresh-token")
    fun refreshToken(
        @Header("Authorization") bearerToken: String,
        @Body emptyBody: Map<String, String> = emptyMap()
    ): Call<ApiSuccessResponse<LoginResponse>>
}
