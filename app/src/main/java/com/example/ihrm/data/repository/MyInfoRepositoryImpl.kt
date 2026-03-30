package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.MyInfoApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.domain.repository.MyInfoRepository
import retrofit2.Retrofit
import javax.inject.Inject

class MyInfoRepositoryImpl @Inject constructor(
    private val myInfoApiService: MyInfoApiService,
    private val retrofit: Retrofit
) : MyInfoRepository {
    override suspend fun getMeEmployeeInfo(): NetworkResult<MyInfo> =
        safeApiCall(retrofit) {
            myInfoApiService.getMe()
        }.map { it.fromResponseToInfo() }

    override suspend fun getMeProfile(): NetworkResult<MyProfile> =
        safeApiCall(retrofit) {
            myInfoApiService.getMeProfile()
        }.map { it.fromResponseToInfo() }
}
