package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.base.ApiSuccessResponse
import com.example.ihrm.data.remote.myinfo.ChangePasswordRequest
import com.example.ihrm.data.remote.myinfo.MeProfileResponse
import com.example.ihrm.data.remote.myinfo.MeResponse
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyInfoApiService {
    @GET("/me")
    suspend fun getMe(): Response<ApiSuccessResponse<MeResponse>>

    @GET("/me/profile")
    suspend fun getMeProfile(): Response<ApiSuccessResponse<MeProfileResponse>>

    @PATCH("/me/profile")
    suspend fun updateMeProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiSuccessResponse<MeProfileResponse>>

    @PATCH("/me")
    suspend fun updateInfoMeProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiSuccessResponse<MeProfileResponse>>

    @POST("me/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ApiSuccessResponse<Unit>>
}