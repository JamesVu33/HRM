package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.myinfo.MeProfileResponse
import com.example.ihrm.data.remote.myinfo.MeResponse
import retrofit2.Response
import retrofit2.http.GET

interface MyInfoApiService {
    @GET("/me")
    suspend fun getMe(): Response<ApiSuccessResponse<MeResponse>>

    @GET("/me/profile")
    suspend fun getMeProfile(): Response<ApiSuccessResponse<MeProfileResponse>>
}