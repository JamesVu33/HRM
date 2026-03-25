package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.language.LanguageRequest
import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.data.remote.login.LoginRequest
import com.example.ihrm.data.remote.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Auth API từ be-nest-hrm.
 * Swagger: https://be-nest-hrm.onrender.com/swagger#
 */
interface LanguageApiService {

    @GET("translations")
    suspend fun language(@QueryMap request: Map<String, String>): Response<ApiSuccessResponse<List<LanguageResponse>>>
}
