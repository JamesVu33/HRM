package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.MyInfoApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.myinfo.MeProfileResponse
import com.example.ihrm.data.remote.myinfo.MeResponse
import com.example.ihrm.domain.repository.MyInfoRepository
import retrofit2.Retrofit
import javax.inject.Inject

class MyInfoRepositoryImpl @Inject constructor(
    private val myInfoApiService: MyInfoApiService,
    private val retrofit: Retrofit
) : MyInfoRepository {
    override suspend fun getMeEmployeeInfo(): NetworkResult<MeResponse> =
        safeApiCall(retrofit) {
            myInfoApiService.getMe()
        }

    override suspend fun getMeProfile(): NetworkResult<MeProfileResponse> =
        safeApiCall(retrofit) {
            myInfoApiService.getMeProfile()
        }
}